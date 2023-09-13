package com.example.admin.controller;

import com.example.admin.model.AdminPage;
import com.example.admin.model.Order;
import com.example.admin.service.AdminSevice;
import com.example.admin.service.IndexService;
import com.example.admin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class Index {
    @Autowired
    private IndexService indexService;

    @Autowired
    private AdminSevice adminService;

    @GetMapping("/")
    public String showHome(Model model) {

        AdminPage loggedInAdmin = adminService.getLoggedInAdmin();
        model.addAttribute("loggedInAdmin", loggedInAdmin);

        double  totalOKOrderAmount = indexService.getTotalAmountForStatus0();
        long totalProductCount = indexService.getTotalProductCount();
        long totalCustomerCount = indexService.getTotalCustomerCount();
        long nullStatusOrderCount = indexService.getNullStatusOrderCount();

        model.addAttribute("totalProductCount", totalProductCount);
        model.addAttribute("totalCustomerCount", totalCustomerCount);
        model.addAttribute("nullStatusOrderCount", nullStatusOrderCount);
        model.addAttribute("totalOKOrderAmount", totalOKOrderAmount);

        return "index";
    }



}

