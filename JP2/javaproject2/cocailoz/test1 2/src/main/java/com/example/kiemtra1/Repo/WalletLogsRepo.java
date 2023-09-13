package com.example.kiemtra1.Repo;

import com.example.kiemtra1.Model.walletLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletLogsRepo extends JpaRepository<walletLogs,Long> {
    List<walletLogs> findWalletLogsByWalletName(String sub);

//    List<Object> findByIdCard(String idCard);
}
