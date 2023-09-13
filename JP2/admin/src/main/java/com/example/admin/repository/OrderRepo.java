package com.example.admin.repository;

import com.example.admin.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.id = :orderId")
    void deleteOrderById(@Param("orderId") Long orderId);

    long countByStatusIsNull();

    @Query("SELECT COALESCE(SUM(o.orderAmount), 0) FROM Order o WHERE o.status = 0")
    String sumOrderAmountByStatus();


    @Query("SELECT o.id AS order_id, p.id AS product_id, ci.id AS cart_item_id, p.productName, p.price " +
            "FROM Order o " +
            "JOIN o.cartItems ci " +
            "JOIN ci.products p " +
            "WHERE o.id = :orderId")
    List<Object[]> findJoinedDataWithProductInfoByOrderId(@Param("orderId") Long orderId);
}
