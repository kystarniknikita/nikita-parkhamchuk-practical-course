package com.example.PaymentService.service;

import com.example.PaymentService.mapper.PaymentMapper;
import com.example.PaymentService.model.dto.PaymentRequest;
import com.example.PaymentService.model.dto.PaymentResponse;
import com.example.PaymentService.model.entity.Payment;
import com.example.PaymentService.model.entity.type.StatusType;
import com.example.PaymentService.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper mapper;

    @Mock
    private RandomNumberService randomNumberService;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentRequest paymentRequest;
    private Payment payment;
    private PaymentResponse paymentResponse;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest(1L, 1L, BigDecimal.valueOf(100.00));

        payment = new Payment();
        payment.setId("payment-123");
        payment.setOrderId(1L);
        payment.setUserId(1L);
        payment.setPaymentAmount(BigDecimal.valueOf(100.00));
        payment.setStatus(StatusType.SUCCESS);
        payment.setTimestamp(Instant.now());

        paymentResponse = new PaymentResponse("payment-123", 1L, 1L, "SUCCESS", Instant.now(), BigDecimal.valueOf(100.00));
    }

    @Test
    void createPayment_ShouldCreatePaymentSuccessfully() {
        when(mapper.toEntity(paymentRequest)).thenReturn(payment);
        when(randomNumberService.determinePaymentStatus()).thenReturn(StatusType.SUCCESS);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(mapper.toDto(payment)).thenReturn(paymentResponse);

        PaymentResponse result = paymentService.createPayment(paymentRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("payment-123");
        assertThat(result.getStatus()).isEqualTo("SUCCESS");

        verify(paymentRepository).save(any(Payment.class));
        verify(paymentEventProducer).publishCreatePaymentEvent(paymentResponse);
    }

    @Test
    void getPaymentsByOrderId_ShouldReturnPayments() {
        List<Payment> payments = Arrays.asList(payment);
        List<PaymentResponse> expectedResponses = Arrays.asList(paymentResponse);

        when(paymentRepository.findByOrderId(1L)).thenReturn(payments);
        when(mapper.toDtos(payments)).thenReturn(expectedResponses);

        List<PaymentResponse> result = paymentService.getPaymentsByOrderId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(1L);

        verify(paymentRepository).findByOrderId(1L);
        verify(mapper).toDtos(payments);
    }

    @Test
    void getPaymentsByUserId_ShouldReturnPayments() {
        List<Payment> payments = Arrays.asList(payment);
        List<PaymentResponse> expectedResponses = Arrays.asList(paymentResponse);

        when(paymentRepository.findByUserId(1L)).thenReturn(payments);
        when(mapper.toDtos(payments)).thenReturn(expectedResponses);

        List<PaymentResponse> result = paymentService.getPaymentsByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(1L);

        verify(paymentRepository).findByUserId(1L);
        verify(mapper).toDtos(payments);
    }
}