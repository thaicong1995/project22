package com.example.kiemtra1.Model;
import com.example.kiemtra1.Token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "memberaccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class MemberAccount implements UserDetails {
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
//    private String idCard;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role ;

    //tokens
    @JsonIgnore
    @OneToMany(mappedBy = "memberAccount")
    private List<Token> tokens;

    // memberAccount
    @JsonIgnore
    @OneToMany(mappedBy = "Account_Order")
    private List<Order> orders;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null ;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

