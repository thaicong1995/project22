package com.example.admin.controller;

import com.example.admin.dto.JoinDataDTO;
import com.example.admin.model.AdminPage;
import com.example.admin.service.AdminSevice;
import com.example.admin.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class Order {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AdminSevice adminService;

    @GetMapping("/view/{orderId}")
    public String getProductByOrderId(@PathVariable Long orderId, Model model) {
        AdminPage loggedInAdmin = adminService.getLoggedInAdmin(); // Lấy thông tin người dùng đã đăng nhập
        model.addAttribute("loggedInAdmin", loggedInAdmin);

        List<JoinDataDTO> joinData = orderService.getJoinedDataWithProductInfoByOrderId(orderId);
        for (JoinDataDTO join : joinData) {
            System.out.println("Order ID: " + join.getOrderId());
            System.out.println("Product ID: " + join.getProductId());
            System.out.println("Cart Item ID: " + join.getCartItemId());
            System.out.println("Product Name: " + join.getProductName());
            System.out.println("Price: " + join.getPrice());
            System.out.println("-------------------------");
        }
        model.addAttribute("list", joinData);
        return "view"; // Assuming "view" is the name of your Thymeleaf view template
    }

    @GetMapping("/order")
    public String showOrder(Model model) {
        AdminPage loggedInAdmin = adminService.getLoggedInAdmin(); // Lấy thông tin người dùng đã đăng nhập
        model.addAttribute("loggedInAdmin", loggedInAdmin);

        List<com.example.admin.model.Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order";
    }

    @PostMapping("/confirm-order/{orderId}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long orderId, @RequestParam String newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok("Order status updated to " + newStatus);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
