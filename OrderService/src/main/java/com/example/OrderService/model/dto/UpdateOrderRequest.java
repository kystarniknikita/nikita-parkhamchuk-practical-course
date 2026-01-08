package com.example.OrderService.model.dto;

import com.example.OrderService.model.entity.OrderStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {
    @Email
    @NotBlank
    private String userEmail;

    @NotBlank
    private OrderStatus status;

    @NotEmpty
    private List<OrderItemRequest> orderItems;
}
