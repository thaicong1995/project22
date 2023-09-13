package com.example.admin.config;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {
    @GetMapping("/get-csrf-token")
    public String getCsrfToken(CsrfToken csrfToken) {
        if (csrfToken != null) {
            return csrfToken.getToken();
        }
        return "CSRF token not available.";
    }
}
