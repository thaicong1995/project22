package com.example.kiemtra1.Controller;

import com.example.kiemtra1.Auth.AuthenticationRequest;
import com.example.kiemtra1.Auth.AuthenticationResponse;
import com.example.kiemtra1.Auth.AuthenticationService;
import com.example.kiemtra1.Auth.RegisterRequest;
import com.example.kiemtra1.Model.MemberAccount;
import com.example.kiemtra1.Model.Role;
import com.example.kiemtra1.Repo.OderRepo;
import com.example.kiemtra1.Service.MemberaccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class MemberAccountController {
    //    http://localhost:8080/user/password/{id}
    @Autowired
    public OderRepo oderRepo;
    @Autowired
    public MemberaccountService memberaccountService;
    @Autowired
    public AuthenticationService service;

    @PostMapping("/add") // user token register
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PutMapping("/update") // use token update
    public ResponseEntity<MemberAccount> updateProduct(@RequestParam String token, @RequestBody MemberAccount memberAccount) {
        MemberAccount account1 = memberaccountService.updateAccount(token,memberAccount);
        return new ResponseEntity<>(account1, HttpStatus.OK);
    }

    @PostMapping("/login")  // use token login
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/password") // if account change to password
    public ResponseEntity<?>updatePW(@RequestParam String token,@RequestBody MemberAccount memberAccount){
        return ResponseEntity.ok(memberaccountService.updatePassword(token, memberAccount));
    }
    @PostMapping("/forgetpassword") // if account forget password
    public boolean forgetPassword(@RequestParam String email , @RequestParam String password) {
        return memberaccountService.forgetpassword(email, password);
    }



}