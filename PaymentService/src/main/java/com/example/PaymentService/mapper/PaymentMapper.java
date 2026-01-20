package com.example.PaymentService.mapper;

import com.example.PaymentService.model.dto.PaymentRequest;
import com.example.PaymentService.model.dto.PaymentResponse;
import com.example.PaymentService.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    Payment toEntity(PaymentRequest request);

    @Mapping(target = "status", expression = "java(entity.getStatus().toString())")
    PaymentResponse toDto(Payment entity);

    List<PaymentResponse> toDtos(List<Payment> entities);
}
