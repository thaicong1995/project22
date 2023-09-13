package com.example.admin.service;

import com.example.admin.model.AdminPage;
import com.example.admin.repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AdminSevice implements UserDetailsService {
    @Autowired
    private AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        AdminPage adminPage = adminRepo.findByUsername(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username"));

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(adminPage.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                adminPage.getUsername(),
                adminPage.getPassword(),
                authorities
        );
    }

    public AdminPage getLoggedInAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<AdminPage> adminOptional = adminRepo.findByUsername(userDetails.getUsername());
            return adminOptional.orElse(null);
        }

        return null;
    }
}
