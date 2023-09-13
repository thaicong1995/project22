package com.example.kiemtra1.Repo;

import com.example.kiemtra1.Model.vnpayinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VnpayRepo extends JpaRepository< vnpayinfo,Long> {
}
