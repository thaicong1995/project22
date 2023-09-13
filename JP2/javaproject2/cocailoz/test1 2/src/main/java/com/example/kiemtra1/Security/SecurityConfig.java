package com.example.kiemtra1.Security;

import com.example.kiemtra1.Model.Role;
import com.example.kiemtra1.Service.OrderSevice;
import com.example.kiemtra1.Token.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   @Autowired
    private JwtAuthenticationFilter jwt;
    public  final AuthenticationProvider  authenticationProvider;
    private final static Logger LOGGER = LoggerFactory
            .getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll());
                http.addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
    //　あしたまたチェクしてください
    /*
    * check security try agin*/
}


