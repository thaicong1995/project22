package com.example.kiemtra1.Repo;

import com.example.kiemtra1.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartitemRepo extends JpaRepository<CartItem,Long> {
    void deleteCartItemById(Long id);

    @Query("SELECT p.productName FROM CartItem c JOIN c.products p WHERE c.id = :cartItemId")
    List<String> findProductNamesByCartItemId(Long cartItemId);


}
