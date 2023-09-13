package com.example.kiemtra1.Repo;

import com.example.kiemtra1.Model.CartItem;
import com.example.kiemtra1.Model.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAccountRepo extends JpaRepository<MemberAccount,Long> {
    Optional<MemberAccount> findByUsername(String username);
    Optional<MemberAccount> findByEmail(String email);
    Optional<MemberAccount> findByPassword(String password);
}

