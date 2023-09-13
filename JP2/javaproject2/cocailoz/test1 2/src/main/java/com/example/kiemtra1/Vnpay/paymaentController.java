package com.example.kiemtra1.Vnpay;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file.log, choose Tools | Templates
 * and open the template in the editor.
 */



import com.example.kiemtra1.DTO.MemberData;
import com.example.kiemtra1.DTO.PaymentDto;
import com.example.kiemtra1.DTO.Transection;
import com.example.kiemtra1.DecodeJWT.JwtDecoder1;
import com.example.kiemtra1.Model.*;
import com.example.kiemtra1.Repo.OderRepo;
import com.example.kiemtra1.Repo.VnpayRepo;
//import com.example.kiemtra1.SendPhone.SendPhone;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.SneakyThrows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.example.kiemtra1.Vnpay.Config.*;


@Controller
public class paymaentController {
    public static Logger logger = LoggerFactory.getLogger(paymaentController.class);
    @Autowired
    public OderRepo oderRepo;
    @Autowired
    public JwtDecoder1 jwtDecoder1;
    @Value("${uploading.videoSaveFolder}")
    private String FOLDER_PATH;
    @Autowired
    public VnpayRepo vnpayRepo;
//    private SendPhone sendPhone;
    @SneakyThrows
    @GetMapping("/pay/{id}")
    public ResponseEntity<?> hashUrl(@RequestParam String token, @PathVariable Long id) {
        MemberData memberData = jwtDecoder1.decode(token);
        Optional<Order> order = oderRepo.findByMembernameAndId(memberData.getSub(), id);
        Long amount = (long) order.get().getOrderAmount();
        String vnp_OrderType = order.get().getOrderNo();
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "192.168.1.15";
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_Command", vnp_Command);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderInfo",  vnp_OrderType);
        vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_OrderType", "Quần áo");
        vnp_Params.put("vnp_BankCode", "NCB");
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        PaymentDto order2 = new PaymentDto();
        order2.setStatus("200");
        order2.setMessage("Successfully");
        order2.setUrl(paymentUrl);
        return ResponseEntity.status(HttpStatus.OK).body(order2);
    }

//    @GetMapping("/domain.vn")
//    public ResponseEntity<?> Transaction(
//            @RequestParam(value = "vnp_Amount") long amount,
//            @RequestParam(value = "vnp_BankCode") String BankCode,
//            @RequestParam(value = "vnp_OrderInfo") String OrderInfo,
//            @RequestParam(value = "vnp_ResponseCode") String responseCode,
//            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
//            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo
//    ){
//    Transection transection = new Transection();
//
//    if(responseCode.equals("00")){
//        transection.setStatus("ok");
//        transection.setMessage("thanh cong");
//        vnpayinfo vnpayinfo1 = new vnpayinfo();
//        vnpayinfo1.setAmount(amount / 100);
//        vnpayinfo1.setBankCode(BankCode);
//        vnpayinfo1.setOrderInfo(OrderInfo);
//        vnpayinfo1.setResponseCode(responseCode);
//        vnpayinfo1.setVnp_TxnRef(vnp_TxnRef);
//        vnpayinfo1.setVnp_TransactionNo(vnp_TransactionNo);
//        logger.info("orderno =>" + vnp_TxnRef + "\n" + "VnPayAmount =>" + amount);
//        vnpayRepo.save(vnpayinfo1);
////        Order order = oderRepo.findByOrderNo(OrderInfo);
////        String phone =order.getOrderNo();
////        String body = "số tiền bạn đã thanh toan la " + amount;
////        sendPhone.Sendphone(phone,body);
//    }
//     else {
//        transection.setStatus("NO");
//        transection.setMessage("that bai");
//        transection.setDate("");
//    }
//        return ResponseEntity.status(HttpStatus.OK).body(transection);
//    }

    @GetMapping("/domain.vn")
    public ResponseEntity<?> Transaction(
            @RequestParam(value = "vnp_Amount") long amount,
            @RequestParam(value = "vnp_BankCode") String BankCode,
            @RequestParam(value = "vnp_OrderInfo") String OrderInfo,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo
    ) {
        Transection transection = new Transection();

        if (responseCode.equals("00")) {


            // Update the order status to "OK" here
            Order order = oderRepo.findByOrderNo(OrderInfo);
            if (order != null) {
                order.setStatus(Status.OK);
                oderRepo.save(order);
            }
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "http://localhost:4200/list").build();
        } else {
            transection.setStatus("Pending");
            transection.setMessage("Thất bại");
            transection.setDate("");

            // Update the order status to "Pending" here
            Order order = oderRepo.findByOrderNo(OrderInfo);
            if (order != null) {
                order.setStatus(Status.Pending);
                oderRepo.save(order);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(transection);
    }


    @GetMapping("/payqr/{id}")
    public ResponseEntity<?> payQr(@RequestParam String token, @PathVariable Long id) throws UnsupportedEncodingException {
        String filePath = "vnpay_qr.png";
        int width = 300;
        int height = 300;
        Optional<Order> order = oderRepo.findById(id);
        Long amount = (long) order.get().getOrderAmount();
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = "192.168.1.15";
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_Command", vnp_Command);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_OrderType", "Quần áo");
//        vnp_Params.put("vnp_BankCode", "NCB");
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(paymentUrl, BarcodeFormat.QR_CODE, width, height);

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int color = bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    bufferedImage.setRGB(x, y, color);
                }
            }
            // Ghi QR code vào ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            // Tạo tệp tạm thời từ ByteArrayOutputStream
            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            // Trả về tệp tạm thời dưới dạng tệp PNG
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vnpay_qr.png");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
