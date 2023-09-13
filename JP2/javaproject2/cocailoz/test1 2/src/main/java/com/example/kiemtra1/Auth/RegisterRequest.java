package com.example.kiemtra1.Auth;

import com.example.kiemtra1.Model.CartItem;
import com.example.kiemtra1.Model.Role;
import com.example.kiemtra1.Token.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String accountNo;
    private String password;
    private String username;
    private String email;
    private String phoneNo;
    private String idCard;
    private String address;
    private Role role = Role.USER;
    private CartItem cartItems;
    private List<Token> tokens;
}
