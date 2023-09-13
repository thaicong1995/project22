package com.example.kiemtra1.DecodeJWT;

import com.example.kiemtra1.DTO.MemberData;
import com.example.kiemtra1.Model.MemberAccount;
import com.example.kiemtra1.Model.Order;
import com.example.kiemtra1.Repo.MemberAccountRepo;
import com.example.kiemtra1.Repo.OderRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@Controller
@RequestMapping("/Decode")
public class JwtDecoder1 {
    @Autowired
    public MemberAccountRepo memberAccountRepo;
    @Autowired
    public OderRepo oderRepo;

    @PostMapping("/jwt1")
    @ResponseBody
    public ResponseEntity<MemberData> decode1(@RequestBody String token) {
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length != 3) {
            // Token không hợp lệ
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String payload = tokenParts[1];
        byte[] decodedPayload = Base64.getDecoder().decode(payload);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MemberData memberData = objectMapper.readValue(decodedPayload, MemberData.class);
            Optional<MemberAccount> foundMember = memberAccountRepo.findByUsername(memberData.getSub());
//            Optional<Order> order = oderRepo.findByMembername(memberData.getSub());
            if (foundMember.isPresent() ) {
                MemberAccount memberAccount = foundMember.get();
                memberData.setId(memberAccount.getId());
//                memberData.setOderno(order.get().getOrderNo());
                return ResponseEntity.ok(memberData);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return null;
    }

    public MemberData decode( String token) {
        String[] tokenParts = token.split("\\.");
        if (tokenParts.length == 3) {

            String payload = tokenParts[1];
            byte[] decodedPayload = Base64.getDecoder().decode(payload);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                MemberData memberData = objectMapper.readValue(decodedPayload, MemberData.class);
                Optional<MemberAccount> foundMember = memberAccountRepo.findByUsername(memberData.getSub());
                if (foundMember.isPresent()) {
                    MemberAccount memberAccount = foundMember.get();
                    memberData.setId(memberAccount.getId());
                    return memberData;
                }

            } catch (Exception e) {
                e.getMessage();
            }
        }
        return null;
    }
}

