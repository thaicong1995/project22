package com.example.admin.controller;
import com.example.admin.model.AdminPage;
import com.example.admin.model.Product;
import com.example.admin.service.AdminSevice;
import com.example.admin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.valueOf;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private AdminSevice adminService;

    @GetMapping("/list")
    public String showAllProduct(Model model) {
        AdminPage loggedInAdmin = adminService.getLoggedInAdmin(); // Lấy thông tin người dùng đã đăng nhập
        model.addAttribute("loggedInAdmin", loggedInAdmin);

        List<Product> products = productService.getAllProduct();
        model.addAttribute("product", products);
        return "productList";
    }


    @GetMapping("/fileSystem/{filename}")// upload web
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String filename) throws IOException {
        byte[] imageData=productService.downloadImageFromFileSystem(filename);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(valueOf("image/png"))
                .body(imageData);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }


}
