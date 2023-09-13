package com.example.kiemtra1.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberData {
    private Long id;
    private String sub;
    private Long iat;
    private Long exp;
    private String password;
    private String oderno;
}
