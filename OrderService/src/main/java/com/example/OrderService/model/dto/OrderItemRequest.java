package com.example.OrderService.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull
    private Long itemId;

    @NotNull
    @Min(1)
    private Integer quantity;
}

