package com.example.admin.service;

import com.example.admin.model.Order;
import com.example.admin.model.Status;
import com.example.admin.repository.CustomeRepo;
import com.example.admin.repository.OrderRepo;
import com.example.admin.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexService {
    @Autowired
    private  ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CustomeRepo customeRepo;
    public long getTotalProductCount() {
        return productRepo.count();
    }

    public long getTotalCustomerCount() {
        return customeRepo.count();
    }

    public long getNullStatusOrderCount() {
        return orderRepo.countByStatusIsNull();
    }

    public double getTotalAmountForStatus0() {
        String totalAmountString = orderRepo.sumOrderAmountByStatus();
        double totalAmount = Double.parseDouble(totalAmountString);
        return totalAmount;
    }
}
