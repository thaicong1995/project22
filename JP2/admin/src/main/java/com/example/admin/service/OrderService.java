package com.example.admin.service;

import com.example.admin.dto.JoinDataDTO;
import com.example.admin.model.Order;
import com.example.admin.model.Status;
import com.example.admin.repository.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            if (newStatus.equals("OK") && order.getStatus() == Status.NotOK) {
                throw new IllegalArgumentException("Cannot change status from NotOK to OK");
            }

            order.setStatus(Status.valueOf(newStatus));  // Cập nhật trạng thái mới
            orderRepository.save(order);                 // Lưu thay đổi vào cơ sở dữ liệu
        } else {
            throw new IllegalArgumentException("Order not found");
        }
    }

    public List<JoinDataDTO> getJoinedDataWithProductInfoByOrderId(Long orderId) {
        List<Object[]> results = orderRepository.findJoinedDataWithProductInfoByOrderId(orderId);
        List<JoinDataDTO> dtoList = new ArrayList<>();

        for (Object[] row : results) {
            JoinDataDTO dto = new JoinDataDTO();
            dto.setOrderId((Long) row[0]);
            dto.setProductId((int) row[1]);
            dto.setCartItemId((Long) row[2]);
            dto.setProductName((String) row[3]);
            dto.setPrice((Double) row[4]);
            dtoList.add(dto);
        }

        return dtoList;
    }


    public void deleteOrderById(Long orderId) {
        orderRepository.deleteOrderById(orderId);
    }

}
