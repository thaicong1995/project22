package com.example.admin.service;

import com.example.admin.model.MemberAccount;
import com.example.admin.model.Order;
import com.example.admin.repository.CustomeRepo;
import com.example.admin.repository.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomeRepo customeRepo;
    public List<MemberAccount> getAllCustomer() {
        return customeRepo.findAll();
    }

    @Transactional
    public void deleteProduct(Long id) {
        customeRepo.deleteCusById(id);
    }
}
