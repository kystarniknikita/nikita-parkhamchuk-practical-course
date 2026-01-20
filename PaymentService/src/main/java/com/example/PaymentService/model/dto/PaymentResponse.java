package com.example.PaymentService.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String id;
    private Long orderId;
    private Long userId;
    private String status;
    private Instant timestamp;
    private BigDecimal paymentAmount;
}
