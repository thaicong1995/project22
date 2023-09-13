package com.example.admin.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "memberaccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MemberAccount  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Column(unique = false)
    private String phoneNo;
    private String idCard;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role ;

}

