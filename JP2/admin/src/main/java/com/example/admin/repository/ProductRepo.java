package com.example.admin.repository;

import com.example.admin.model.Order;
import com.example.admin.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    Optional<Product> findById(Long id);
    Optional<Product> findByImg(String fileName);
    @Transactional
    void deleteProductById(Long id);
}
