package com.example.kiemtra1.Service;

import com.example.kiemtra1.DecodeJWT.JwtDecoder1;
import com.example.kiemtra1.DTO.MemberData;
import com.example.kiemtra1.Email.EmailService;
import com.example.kiemtra1.Model.*;

import com.example.kiemtra1.Repo.*;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class OrderSevice {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(OrderSevice.class);
    @Autowired
    public OderRepo oderRepo;
    @Autowired
    public MemberAccountRepo memberAccountRepo;
    @Autowired
    public WalletRepo walletRepo;
    @Autowired
    public WalletLogsRepo walletLogsRepo;
    @Autowired
    public EmailService emailService;
    @Autowired
    public JwtDecoder1 jwtDecoder1;
    @Autowired
    public CartitemRepo cartitemRepo;
    @Autowired
    public ProductRepo productRepo;
    public Logger logger = LoggerFactory.getLogger(OrderSevice.class);

    // Update order
    public Order updateOrder(Long id, Order order, String token) {
        MemberData memberData = jwtDecoder1.decode(token);
        Optional<MemberAccount> memberAccount = memberAccountRepo.findById(memberData.getId());
        return oderRepo.findById(id)
                .map(order1 -> {
                    Random random = new Random();
                    int randomNumber = random.nextInt(1000);
                    String oderno = "ORDER" + String.format("%03d", randomNumber);
                    order1.setOrderNo(oderno);
                    order1.setMembername(memberData.getSub());
                    order1.setEmail(order.getEmail());
                    memberAccount.ifPresent(order1::setAccount_Order);
                    order1.setPhoneNo(order.getPhoneNo());
                    order1.setAddress(order.getAddress());
                    order1.setOrderDate(LocalDateTime.now());
                    logger.info(" update Order for MemberAccount :" + memberData.getSub());
                    return oderRepo.save(order1);
                })
                .orElse(null); // Return null if order with the given id is not found
    }


    // list story walletlogs + order
    /*
    cartitem + product + order + walletlogs

    * */
    // create story for order
    public List<Order> getAllOrders(String token) {
        MemberData memberData = jwtDecoder1.decode(token);
        logger.info("Getting all orders for MemberAccount: " + memberData.getSub());

        List<Order> orders = oderRepo.findOrdersByMembername(memberData.getSub());
        if (orders != null) {
            return oderRepo.findByStatus(Status.OK);
        } else {
            return null;
        }
    }
public Optional<Order> getStroyProduct(Long id){
     Optional<Order> order = oderRepo.findById(id);
    logger.info(" get Story Product  for Order :" + id );
    return order;
}
    public double payment1( String token, long id) {
        MemberData memberData = jwtDecoder1.decode(token);
        wallet wallet1 = walletRepo.findByUsername(memberData.getSub());
            if (wallet1 != null) {
                Optional<Order> order1 = oderRepo.findById(id);
                Order order = order1.get();
                double orderAmount = order.getOrderAmount();
                double currentBalance = wallet1.getAmount();
                if (checkamount(currentBalance, orderAmount)) {
                    double newBalance = currentBalance - orderAmount;
                    wallet1.setAmount(newBalance);
                    walletRepo.save(wallet1);
                    walletLogs walletLogs = new walletLogs();
                    walletLogs.setAmount(newBalance);
                    walletLogs.setOrder_id(id);
                    walletLogs.setDate(LocalDateTime.now());
                    walletLogs.setStatus(true);
                    walletLogs.setWalletName(memberData.getSub());
                    walletLogs.setAmount_deducted(orderAmount);
                    order.setStatus(Status.OK);
                    logger.info("Acoount name :" +  memberData.getSub() + "\n " + "OrderAmount :" + orderAmount +"\n"+ "OrderNo :"+ order.getOrderNo() );
                    walletLogsRepo.save(walletLogs);
                    List<String> cartItem = cartitemRepo.findProductNamesByCartItemId(id);

                    Random random = new Random();
                    int randomNumber = random.nextInt(1000);
                    String magiaodich = "MGD" + String.format("%03d", randomNumber);
                    String to = order.getEmail();
                            String emailContent = "<!DOCTYPE html>\n" +
                                    "<html>\n" +
                                    "<head>\n" +
                                    "    <title>Thông báo thanh toán thành công</title>\n" +
                                    "    <style>\n" +
                                    "        body {\n" +
                                    "            font-family: Arial, sans-serif;\n" +
                                    "            background-color: #f4f4f4;\n" +
                                    "            margin: 0;\n" +
                                    "            padding: 0;\n" +
                                    "        }\n" +
                                    "\n" +
                                    "        .container {\n" +
                                    "            max-width: 600px;\n" +
                                    "            margin: 0 auto;\n" +
                                    "            padding: 20px;\n" +
                                    "            background-color: #fff;\n" +
                                    "            border: 1px solid #ccc;\n" +
                                    "            border-radius: 5px;\n" +
                                    "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
                                    "        }\n" +
                                    "\n" +
                                    "        h1 {\n" +
                                    "            color: #333;\n" +
                                    "        }\n" +
                                    "\n" +
                                    "        p {\n" +
                                    "            color: #666;\n" +
                                    "        }\n" +
                                    "\n" +
                                    "        .success-message {\n" +
                                    "            background-color: #dff0d8;\n" +
                                    "            color: #3c763d;\n" +
                                    "            border: 1px solid #d6e9c6;\n" +
                                    "            padding: 10px;\n" +
                                    "            margin-top: 20px;\n" +
                                    "            border-radius: 5px;\n" +
                                    "        }\n" +
                                    "\n" +
                                    "        .transaction-code {\n" +
                                    "            font-weight: bold;\n" +
                                    "            margin-top: 10px;\n" +
                                    "        }\n" + ".order-amount {\n" +
                                    "            font-size: 18px;\n" +
                                    "            margin-top: 10px;\n" +
                                    "        } " +

                                    "    </style>\n" +
                                    "</head>\n" +
                                    "<body>\n" +
                                    "    <div class=\"container\">\n" +
                                    "        <h1>Thanh toán thành công! </h1>\n" +
                                    order.getOrderNo() + " <p>Đơn hàng của bạn đã được thanh toán thành công.</p>\n" +
                                    "\n"+ cartItem +
                                    " <p>Tổng tiền :</p>\n" + orderAmount +
                                    "\n"+
                                    "        <div class=\"success-message\">\n" + memberData.getSub() +
                                    "            <p>   Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>\n" +
                                    "        </div>\n" +
                                    "\n" +
                                    "        <p class=\"transaction-code\">Mã giao dịch:  </p>\n" + magiaodich +
                                    "<p class=\"order-amount\">Số tiền đơn hàng: </p>" + order.getOrderAmount() +
                                    "    </div>\n" +
                                    "</body>\n" +
                                    "</html>\n";

                            emailService.send(to, "Confirm your email", emailContent);
                            return newBalance;

                        }
                    }
        logger.warn("Wallet not found for user: {}", memberData.getSub());
        return -1;
    }

    public boolean checkamount (double amount , double oderomunt){
        if (amount >= oderomunt){
            return true;
        }
        else {
            logger.warn("Insufficient balance for payment.");
            return false;
        }
    }
}











