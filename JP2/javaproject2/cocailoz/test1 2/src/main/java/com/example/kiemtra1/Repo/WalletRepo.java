package com.example.kiemtra1.Repo;

import com.example.kiemtra1.Model.wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<wallet,Long> {
    wallet findByIdCard(String idCard);


    wallet findByUsername(String username);
}
