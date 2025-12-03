package com.example.OrderService.mapper;

import com.example.OrderService.model.dto.OrderItemResponse;
import com.example.OrderService.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    @Mapping(source = "item.id", target = "itemId")
    OrderItemResponse toResponse(OrderItem orderItem);

    List<OrderItemResponse> toResponseList(List<OrderItem> orderItems);
}
