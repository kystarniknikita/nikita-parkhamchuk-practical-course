package com.example.PaymentService.controller;

import com.example.PaymentService.model.dto.*;
import com.example.PaymentService.model.entity.type.StatusType;
import com.example.PaymentService.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Creating payment for order: {}", request.getOrderId());

        PaymentResponse response = paymentService.createPayment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrderId(@PathVariable Long orderId) {
        log.info("Getting payments for order: {}", orderId);

        List<PaymentResponse> payments = paymentService.getPaymentsByOrderId(orderId);

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUserId(@PathVariable Long userId) {
        log.info("Getting payments for user: {}", userId);

        List<PaymentResponse> payments = paymentService.getPaymentsByUserId(userId);

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatuses(@RequestParam List<StatusType> statuses) {
        log.info("Getting payments by statuses: {}", statuses);

        List<PaymentResponse> payments = paymentService.getPaymentsByStatuses(statuses);

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/summary")
    public ResponseEntity<PaymentSummaryResponse> getPaymentsSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate) {

        log.info("Getting payments summary from {} to {}", startDate, endDate);

        PaymentSummaryResponse summary = paymentService.getTotalPaymentsForDatePeriod(startDate, endDate);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String paymentId) {
        log.info("Getting payment by id: {}", paymentId);

        return paymentService.findPaymentById(paymentId)
                .map(payment -> ResponseEntity.ok(payment))
                .orElse(ResponseEntity.notFound().build());
    }
}