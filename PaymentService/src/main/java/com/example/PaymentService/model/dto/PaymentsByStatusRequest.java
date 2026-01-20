package com.example.PaymentService.model.dto;

import com.example.PaymentService.model.entity.type.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsByStatusRequest {

    @NotNull
    private List<StatusType> statuses;
}