package com.example.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinDataDTO {
    private Long orderId;
    private Integer productId;
    private Long cartItemId;
    private String productName;
    private double price;
}