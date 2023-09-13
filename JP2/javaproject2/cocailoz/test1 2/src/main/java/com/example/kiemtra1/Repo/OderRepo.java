package com.example.kiemtra1.Repo;

import com.example.kiemtra1.Model.Order;
import com.example.kiemtra1.Model.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OderRepo extends JpaRepository<Order,Long> {
    Optional<Order> findByMembername(String membername);

    List<Order> findOrdersByMembername(String sub);

    Optional<Order> findByMembernameAndId(String sub, Long id);

    List<Order> findByStatus(Status status);

    Order findByOrderNo(String orderInfo);
}
