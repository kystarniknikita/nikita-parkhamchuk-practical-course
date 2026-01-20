package com.example.OrderService.service;

import com.example.OrderService.event.CreatePaymentEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "create-payment-events", groupId = "order-service")
    public void handleCreatePaymentEvent(String message) {
        try {
            log.info("Received CREATE_PAYMENT event: {}", message);

            CreatePaymentEvent paymentEvent = objectMapper.readValue(message, CreatePaymentEvent.class);

            handlePaymentCreated(paymentEvent);

            log.info("Successfully processed payment event for order: {}", paymentEvent.getOrderId());

        } catch (JsonProcessingException e) {
            log.error("Error parsing CREATE_PAYMENT event message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing CREATE_PAYMENT event: {}", e.getMessage());
        }
    }

    private void handlePaymentCreated(CreatePaymentEvent paymentEvent) {
        log.info("Payment {} for order {} with status: {}",
                paymentEvent.getPaymentId(),
                paymentEvent.getOrderId(),
                paymentEvent.getStatus());
    }
}