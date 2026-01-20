package com.example.OrderService.service;

import com.example.OrderService.event.CreateOrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String CREATE_ORDER_TOPIC = "create-order-events";

    public void publishCreateOrderEvent(Long orderId, Long userId, BigDecimal totalAmount) {
        try {
            CreateOrderEvent event = new CreateOrderEvent(
                    orderId,
                    userId,
                    totalAmount,
                    Instant.now()
            );

            String message = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(CREATE_ORDER_TOPIC, orderId.toString(), message);

            log.info("Published CREATE_ORDER event for order id: {}", orderId);

        } catch (Exception e) {
            log.error("Error publishing CREATE_ORDER event: {}", e.getMessage());
        }
    }
}