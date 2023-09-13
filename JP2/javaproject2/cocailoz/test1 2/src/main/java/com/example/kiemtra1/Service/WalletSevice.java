package com.example.kiemtra1.Service;

import com.example.kiemtra1.DecodeJWT.JwtDecoder1;
import com.example.kiemtra1.DTO.MemberData;
import com.example.kiemtra1.Model.MemberAccount;
import com.example.kiemtra1.Model.wallet;
import com.example.kiemtra1.Repo.MemberAccountRepo;
import com.example.kiemtra1.Repo.WalletRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;


@Service
@Transactional
public class WalletSevice {
    @Autowired
    private MemberAccountRepo memberAccountRepo;
    @Autowired
    public WalletRepo walletRepo;
    @Autowired
    public JwtDecoder1 jwtDecoder1;
    private final Logger logger = LoggerFactory.getLogger(WalletSevice.class);

        public double recharge(String idCard, double recharge, String token) {
            wallet wallet = walletRepo.findByIdCard(idCard);
            MemberData memberData = jwtDecoder1.decode(token);
            Optional<MemberAccount> memberAccountOptional = memberAccountRepo.findByUsername(memberData.getSub());
            if (wallet != null && memberAccountOptional.isPresent()) {
                double currentBalance = wallet.getAmount();
                double newBalance = currentBalance + recharge;
                wallet.setAmount(newBalance);
                walletRepo.save(wallet);
                logger.info(memberData.getSub() + "recharge success"  + LocalTime.now());
                return newBalance;
            }
            logger.warn(memberData.getSub() + "recharge not  success"   + LocalTime.now());
            return -1;
        }

    public double getamount( String token){
        MemberData memberData = jwtDecoder1.decode(token);
        wallet wallet1 = walletRepo.findByUsername(memberData.getSub());
        return wallet1.getAmount();
    }
}



