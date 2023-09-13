package com.example.admin.controller;

import com.example.admin.model.AdminPage;
import com.example.admin.model.Product;
import com.example.admin.service.AdminSevice;
import com.example.admin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.valueOf;

@Controller
public class Edit {

    @Autowired
    private ProductService productService;

    @Autowired
    private AdminSevice adminService;

    @GetMapping("/edit/{id}")
    public String showUpload(@PathVariable int id, Model model) {
        AdminPage loggedInAdmin = adminService.getLoggedInAdmin(); // Lấy thông tin người dùng đã đăng nhập
        model.addAttribute("loggedInAdmin", loggedInAdmin);

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable int id, @ModelAttribute("product") Product updatedProduct) {
        productService.updateProduct(id, updatedProduct);
        return "redirect:/list"; // Redirect to the product list page
    }

}
