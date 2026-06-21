package com.foodapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String userName;
    private Double totalAmount;
    private String status;
    private String paymentStatus;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
}
