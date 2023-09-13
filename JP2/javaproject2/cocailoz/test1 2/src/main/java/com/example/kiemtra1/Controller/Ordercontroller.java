package com.example.kiemtra1.Controller;

import com.example.kiemtra1.DTO.OderDto;
import com.example.kiemtra1.Model.Order;
import com.example.kiemtra1.Repo.OderRepo;
import com.example.kiemtra1.Repo.WalletLogsRepo;
import com.example.kiemtra1.Service.OrderSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class Ordercontroller {
    //    http://localhost:8080/order/payment/update/{id}
    @Autowired
    public OrderSevice orderSevice;
    @Autowired
    public OderRepo oderRepo;
    @Autowired
    public WalletLogsRepo walletLogsRepo;

    @PostMapping("/update/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                             @RequestBody Order order,
                                             @RequestParam String token) {
        Order order1 = orderSevice.updateOrder(id,order,token);
        return new ResponseEntity<>(order1, HttpStatus.OK);
    }

    @PostMapping("/payment/{id}")
    public ResponseEntity<OderDto> payment1(
                                            @RequestParam("token") String token,
                                            @PathVariable("id") Long id) {
        double remainingAmount = orderSevice.payment1(  token ,id);
        if (remainingAmount >= 0) {
            OderDto oderDto = new OderDto();
            oderDto.setStatus("ok");
            oderDto.setMessage("Successfully");
            oderDto.setDate(token);
            return ResponseEntity.status(HttpStatus.OK).body(oderDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OderDto("not ok","you don't have enough money",null));
        }
    }

    // get all data table order user token findby usernaame when result take all

    @GetMapping("/story")
    public ResponseEntity<?> getAllOrdersForMember(@RequestParam ("token") String token) {
        List<Order> orders = orderSevice.getAllOrders(token);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
// get all story order but find by id for order_id when result take all
    @GetMapping("/story/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Optional<Order> order = orderSevice.getStroyProduct(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
