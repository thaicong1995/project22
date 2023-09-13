package com.example.admin.repository;

import com.example.admin.model.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomeRepo extends JpaRepository<MemberAccount, Long> {
    void deleteCusById(Long id);
}
