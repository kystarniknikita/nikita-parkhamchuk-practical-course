package com.example.PaymentService.service;

import com.example.PaymentService.mapper.PaymentMapper;
import com.example.PaymentService.model.dto.*;
import com.example.PaymentService.model.entity.Payment;
import com.example.PaymentService.model.entity.type.StatusType;
import com.example.PaymentService.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final RandomNumberService randomNumberService;
    private final PaymentEventProducer paymentEventProducer;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Creating payment for order: {}, user: {}", request.getOrderId(), request.getUserId());

        Payment payment = mapper.toEntity(request);
        payment.setTimestamp(Instant.now());

        StatusType status = randomNumberService.determinePaymentStatus();
        payment.setStatus(status);

        Payment savedPayment = paymentRepository.save(payment);

        log.info("Payment created with id: {}, status: {}", savedPayment.getId(), savedPayment.getStatus());

        PaymentResponse response = mapper.toDto(savedPayment);

        paymentEventProducer.publishCreatePaymentEvent(response);

        return response;
    }

    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        log.info("Finding payments by order id: {}", orderId);

        List<Payment> payments = paymentRepository.findByOrderId(orderId);

        return mapper.toDtos(payments);
    }

    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        log.info("Finding payments by user id: {}", userId);

        List<Payment> payments = paymentRepository.findByUserId(userId);

        return mapper.toDtos(payments);
    }

    public List<PaymentResponse> getPaymentsByStatuses(List<StatusType> statuses) {
        log.info("Finding payments by statuses: {}", statuses);

        List<Payment> payments = paymentRepository.findByStatusIn(statuses);

        return mapper.toDtos(payments);
    }

    public PaymentSummaryResponse getTotalPaymentsForDatePeriod(Instant startDate, Instant endDate) {
        log.info("Getting payment summary for period: {} to {}", startDate, endDate);

        PaymentRepository.PaymentSummaryResult summary = paymentRepository.getTotalPaymentsForDatePeriod(startDate, endDate);

        if (summary != null) {
            return new PaymentSummaryResponse(
                    Optional.ofNullable(summary.getTotalAmount()).orElse(BigDecimal.ZERO),
                    Optional.ofNullable(summary.getCount()).orElse(0L)
            );
        }

        return new PaymentSummaryResponse(BigDecimal.ZERO, 0L);
    }

    public Optional<PaymentResponse> findPaymentById(String paymentId) {
        log.info("Finding payment by id: {}", paymentId);

        return paymentRepository.findById(paymentId)
                .map(mapper::toDto);
    }
}
