package com.example.OrderService.repository;

import com.example.OrderService.model.entity.Order;
import com.example.OrderService.model.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByIdIn(List<Long> ids);

    List<Order> findByStatusIn(List<OrderStatus> statuses);
}
