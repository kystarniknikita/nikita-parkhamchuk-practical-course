package com.example.PaymentService.service;

import com.example.PaymentService.event.CreateOrderEvent;
import com.example.PaymentService.model.dto.PaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "create-order-events", groupId = "payment-service")
    public void handleCreateOrderEvent(String message) {
        try {
            log.info("Received CREATE_ORDER event: {}", message);

            CreateOrderEvent orderEvent = objectMapper.readValue(message, CreateOrderEvent.class);

            PaymentRequest paymentRequest = new PaymentRequest(
                    orderEvent.getOrderId(),
                    orderEvent.getUserId(),
                    orderEvent.getTotalAmount()
            );

            paymentService.createPayment(paymentRequest);

            log.info("Successfully processed payment for order: {}", orderEvent.getOrderId());

        } catch (JsonParseException e) {
            log.error("Error parsing CREATE_ORDER event message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing CREATE_ORDER event: {}", e.getMessage());
        }
    }
}