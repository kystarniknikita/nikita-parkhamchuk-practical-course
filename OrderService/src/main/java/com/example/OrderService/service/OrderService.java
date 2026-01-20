package com.example.OrderService.service;

import com.example.OrderService.exception.ResourceNotFoundException;
import com.example.OrderService.mapper.OrderMapper;
import com.example.OrderService.model.dto.*;
import com.example.OrderService.model.entity.Item;
import com.example.OrderService.model.entity.Order;
import com.example.OrderService.model.entity.OrderItem;
import com.example.OrderService.model.entity.OrderStatus;
import com.example.OrderService.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final OrderMapper orderMapper;
    private final UserService userService;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        UserInfoResponse user = userService.getByEmail(request.getUserEmail());

        System.out.println(user.getId());
        System.out.println(user.getEmail());
        System.out.println(user.getName());
        Order order = orderMapper.toEntity(request);

        order.setUserId(user.getId());

        order.setItems(setOrderItems(request.getOrderItems(), order));

        Order saved = orderRepository.save(order);

        OrderResponse response = buildOrderResponse(saved);

        BigDecimal totalAmount = calculateTotalAmount(saved.getItems());
        orderEventProducer.publishCreateOrderEvent(saved.getId(), saved.getUserId(), totalAmount);

        return response;
    }

    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));

        return buildOrderResponse(order);
    }

    public List<OrderResponse> findByIds(List<Long> ids) {
        return orderRepository.findAllByIdIn(ids).stream()
                .map(this::buildOrderResponse)
                .toList();
    }

    public List<OrderResponse> findByStatuses(List<OrderStatus> statuses) {
        return orderRepository.findByStatusIn(statuses).stream()
                .map(this::buildOrderResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateById(Long id, UpdateOrderRequest request) {
        Order existing = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
        UserInfoResponse user = userService.getByEmail(request.getUserEmail());
        existing.setStatus(request.getStatus());
        existing.setUserId(user.getId());
        existing.setItems(setOrderItems(request.getOrderItems(), existing));

        Order saved = orderRepository.save(existing);

        return buildOrderResponse(saved);
    }

    @Transactional
    public void deleteById(Long id) {
        Order existing = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", id));
        orderRepository.delete(existing);
    }

    private OrderResponse buildOrderResponse(Order order) {
        UserInfoResponse userResponse = userService.getById(order.getUserId());
        OrderResponse orderResponse = orderMapper.toResponse(order);

        orderResponse.setUser(userResponse);

        return orderResponse;
    }

    private List<OrderItem> setOrderItems(List<OrderItemRequest> orderItemRequests, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            Item item = itemService.getById(orderItemRequest.getItemId());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(orderItemRequest.getQuantity());

            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}