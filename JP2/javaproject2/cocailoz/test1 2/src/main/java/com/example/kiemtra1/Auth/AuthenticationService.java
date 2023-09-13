package com.example.kiemtra1.Auth;


import com.example.kiemtra1.Email.EmailService;
import com.example.kiemtra1.Model.MemberAccount;
import com.example.kiemtra1.Repo.CartitemRepo;
import com.example.kiemtra1.Repo.MemberAccountRepo;
import com.example.kiemtra1.Repo.WalletRepo;
import com.example.kiemtra1.Token.JwtService;
import com.example.kiemtra1.Token.Token;
import com.example.kiemtra1.Token.TokenRepository;
import com.example.kiemtra1.Token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final MemberAccountRepo memberAccountRepo;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final WalletRepo walletRepo;
    @Autowired
    public EmailService emailService;
    @Autowired
    public CartitemRepo cartItemRepo;
//    @Value("${logger.simpleformast}")
//    private String loggerpath;


    private void saveUserToken(MemberAccount memberAccount, String jwtToken) {
        var token = Token.builder()
                .memberAccount(memberAccount)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
//    public List<CartItem> getCartItemsByMemberAccountId(Long memberAccountId) {
//        return cartItemRepo.findByMemberAccountId(memberAccountId);
//    }

    public AuthenticationResponse register(RegisterRequest request) {

        var user = MemberAccount.builder()
                .username(request.getUsername())
                .address(request.getAddress())
                .email(request.getEmail())
                .phoneNo(request.getPhoneNo())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        logger.info("Account register success" + user.getUsername()  +"&&"+ "time account register " + LocalTime.now());

        var savedUser = memberAccountRepo.save(user);
        var wallet = com.example.kiemtra1.Model.wallet.builder()
                .username(request.getUsername())
                .address(user.getAddress())
                .phoneNo(user.getPhoneNo())
                .build();
        UUID uuid = UUID.randomUUID();
        String idCard = uuid.toString();
        wallet.setIdCard(idCard);
        walletRepo.save(wallet);
        String to = user.getEmail();
        String emailContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Đăng ký thành công</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #008000;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Đăng ký thành công</h1>\n" +
                "    <p>Cảm ơn bạn đã đăng ký thành công!</p>\n" +
                "    <p>Bạn có thể đăng nhập vào tài khoản của mình ngay bây giờ.</p>\n" +
                "</body>\n" +
                "</html>\n";

        emailService.send(to, "Confirm your email", emailContent);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /*
     *  dang ky da xong
     * */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = memberAccountRepo.findByUsername(request.getUsername())
                .orElseThrow();
        logger.info("Account register success" + user.getUsername()  +"&&"+ "time account register " + LocalTime.now());
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    private void revokeAllUserTokens(MemberAccount user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.memberAccountRepo.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, (UserDetails) user)) {
                var accessToken = jwtService.generateToken((UserDetails) user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }


        }
    }
}

