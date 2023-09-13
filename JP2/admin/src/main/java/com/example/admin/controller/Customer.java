package com.example.admin.controller;

import com.example.admin.model.AdminPage;
import com.example.admin.model.MemberAccount;
import com.example.admin.model.Order;
import com.example.admin.service.AdminSevice;
import com.example.admin.service.CustomerService;
import com.example.admin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Controller
public class Customer {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AdminSevice adminService;

    @GetMapping("/customer")
    public String showOrder(Model model) {
        AdminPage loggedInAdmin = adminService.getLoggedInAdmin(); // Lấy thông tin người dùng đã đăng nhập
        model.addAttribute("loggedInAdmin", loggedInAdmin);

        List<MemberAccount> memberAccounts = customerService.getAllCustomer();
        model.addAttribute("customer", memberAccounts);
        return "customer";
    }

    @DeleteMapping("/deletecus/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
