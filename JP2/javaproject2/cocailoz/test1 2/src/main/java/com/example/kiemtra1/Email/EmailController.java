package com.example.kiemtra1.Email;

import com.example.kiemtra1.Model.MemberAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    private final MemberAccount memberAccount;

    public EmailController(MemberAccount memberAccount) {
        this.memberAccount = memberAccount;
    }

}
