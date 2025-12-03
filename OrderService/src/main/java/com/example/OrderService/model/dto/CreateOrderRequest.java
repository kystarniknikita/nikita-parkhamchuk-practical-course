package com.example.OrderService.model.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @Email
    @NotBlank
    private String userEmail;


    @NotEmpty
    private List<OrderItemRequest> orderItems;
}
