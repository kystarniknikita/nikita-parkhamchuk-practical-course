package com.example.OrderService.mapper;


import com.example.OrderService.model.dto.CreateOrderRequest;
import com.example.OrderService.model.dto.OrderResponse;
import com.example.OrderService.model.entity.Order;
import com.example.OrderService.model.entity.OrderStatus;
import org.mapstruct.*;


@Mapper(componentModel = "spring", uses = {OrderItemMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {OrderStatus.class})
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(OrderStatus.PENDING)")
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(CreateOrderRequest request);

    OrderResponse toResponse(Order order);
}
