package com.example.OrderService.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private Long itemId;
    private Integer quantity;
}

