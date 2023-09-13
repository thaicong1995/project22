package com.example.admin.controller;

import com.example.admin.model.AdminPage;
import com.example.admin.model.Product;
import com.example.admin.service.AdminSevice;
import com.example.admin.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Controller
public class Upload {
    @Autowired
    public ProductService productService;

    @Autowired
    private AdminSevice adminService;

    @GetMapping("/upload")
    public String showUpload(Model model) {
        model.addAttribute("product", new Product());
        AdminPage loggedInAdmin = adminService.getLoggedInAdmin();
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        return "upload";
    }

    @PostMapping(value = {"/add"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("image") MultipartFile image,
                                Model model) {
        try {
            productService.addProduct(product, image);
            model.addAttribute("successMessage", "Thêm sản phẩm thành công!");
            return "redirect:/upload?success";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getMessage());
            return "error-page";
        }
    }
}
