package com.example.admin.repository;

import com.example.admin.model.AdminPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  AdminRepo extends JpaRepository<AdminPage, Long> {
    Optional<AdminPage> findByUsername(String username);
    boolean existsByUsername(String username);
}
