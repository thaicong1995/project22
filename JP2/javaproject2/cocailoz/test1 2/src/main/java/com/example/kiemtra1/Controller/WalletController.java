package com.example.kiemtra1.Controller;

import com.example.kiemtra1.Service.WalletSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/wallet")
public class WalletController {
    //http://localhost:8080/wallet/amount
    @Autowired
    public WalletSevice walletService;
    @PostMapping("/recharge")
    public ResponseEntity<Double> rechargeWallet(@RequestParam("token") String token ,
                                                 @RequestParam("idCard") String idCard,
                                                 @RequestParam("recharge") double recharge) {
        double newBalance = walletService.recharge(idCard, recharge, token);
        if (newBalance > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(newBalance);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0.0);
        }
    }
    @GetMapping("/amount")
    public ResponseEntity<Double> amount(@RequestParam String token){
        double amount  = walletService.getamount(token);
        return ResponseEntity.status(HttpStatus.OK).body(amount);
    }
}

