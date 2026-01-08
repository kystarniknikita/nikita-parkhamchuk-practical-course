package com.example.OrderService.controller;

import com.example.OrderService.model.dto.CreateOrderRequest;
import com.example.OrderService.model.dto.OrderResponse;
import com.example.OrderService.model.dto.UpdateOrderRequest;
import com.example.OrderService.model.entity.OrderStatus;
import com.example.OrderService.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        OrderResponse resp = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getByIdsOrStatuses(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) List<OrderStatus> statuses) {

        if (ids != null && !ids.isEmpty()) {
            return ResponseEntity.ok(orderService.findByIds(ids));
        }
        if (statuses != null && !statuses.isEmpty()) {
            return ResponseEntity.ok(orderService.findByStatuses(statuses));
        }
        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id,
                                                @RequestBody @Valid UpdateOrderRequest request) {
        return ResponseEntity.ok(orderService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
