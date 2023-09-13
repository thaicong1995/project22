package com.example.kiemtra1.Service;

import com.example.kiemtra1.DecodeJWT.JwtDecoder1;
import com.example.kiemtra1.DTO.MemberData;
import com.example.kiemtra1.Model.MemberAccount;

import com.example.kiemtra1.Repo.MemberAccountRepo;
import com.example.kiemtra1.Repo.WalletRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MemberaccountService {
    public Logger logger = LoggerFactory.getLogger(MemberaccountService.class);
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    public MemberAccountRepo memberAccountRepo;

    @Autowired
    public JwtDecoder1 jwtDecoder1;
    @Autowired
    public BCryptPasswordEncoder passwordEncoder;

    public MemberAccount updateAccount(String token, MemberAccount newAccount) {
        MemberData memberData = jwtDecoder1.decode(token);
        return memberAccountRepo.findById(memberData.getId())
                .map(account -> {
                    if (newAccount.getEmail() != null) {
                        account.setEmail(newAccount.getEmail());
                    }
                    if (newAccount.getAddress() != null) {
                        account.setAddress(newAccount.getAddress());
                        ;
                    }
                    if (newAccount.getPhoneNo() != null) {
                        account.setPhoneNo(newAccount.getPhoneNo());
                    }
                    logger.info(" update Memberaccount successfully " + memberData.getSub());
                    return memberAccountRepo.save(account);
                })
                .orElseGet(() -> {
                    newAccount.setId(newAccount.getId());
                    return memberAccountRepo.save(newAccount);

                });
    }

    public Boolean updatePassword(String token, MemberAccount newAccount) {
        MemberData memberData = jwtDecoder1.decode(token);
        String ps = memberData.getPassword();
        String newpw = passwordEncoder.encode(newAccount.getPassword());
        if (passwordEncoder.matches(newAccount.getPassword(), ps)) {
            logger.warn("update password with the same password" + memberData.getSub());
            return false;
        } else
            return memberAccountRepo.findById(memberData.getId())
                    .map(account -> {
                        account.setPassword(newpw);
                        memberAccountRepo.save(account);
                        logger.info("Password updated successfully for user with ID: {}", memberData.getId());
                        return true;
                    })
                    .orElseGet(() -> {
                        newAccount.setId(newAccount.getId());
                        memberAccountRepo.save(newAccount);
                        return false;

                    });
    }


    public boolean forgetpassword(String email, String password) {
        Optional<MemberAccount> memberAccount = memberAccountRepo.findByEmail(email);
        if (memberAccount.isPresent()) {
            MemberAccount memberAccount1 = memberAccount.get();
            memberAccount1.setPassword(passwordEncoder.encode(password));
            memberAccountRepo.save(memberAccount1);
            logger.info("forget password to take successfully " + email);
            return true;
        }
        logger.warn("forget password to take not successfully " + email);
        return false;
    }

}


