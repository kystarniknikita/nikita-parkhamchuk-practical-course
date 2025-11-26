package com.example.OrderService.model.dto;

import com.example.OrderService.model.entity.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private OrderStatus status;
    private LocalDateTime creationDate;
    private List<OrderItemResponse> items;
    private UserInfoResponse user;
}
