package com.example.PaymentService.service;

import com.example.PaymentService.event.CreatePaymentEvent;
import com.example.PaymentService.model.dto.PaymentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    private static final String CREATE_PAYMENT_TOPIC = "create-payment-events";
    
    public void publishCreatePaymentEvent(PaymentResponse payment) {
        try {
            CreatePaymentEvent event = new CreatePaymentEvent(
                payment.getId(),
                payment.getOrderId(),
                payment.getUserId(),
                payment.getStatus(),
                payment.getPaymentAmount(),
                payment.getTimestamp()
            );
            
            String message = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send(CREATE_PAYMENT_TOPIC, payment.getId(), message);
            
            log.info("Published CREATE_PAYMENT event for payment id: {}", payment.getId());
            
        } catch (Exception e) {
            log.error("Error publishing CREATE_PAYMENT event: {}", e.getMessage());
        }
    }
}