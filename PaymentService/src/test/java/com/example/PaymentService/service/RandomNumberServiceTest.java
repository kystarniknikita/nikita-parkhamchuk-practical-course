package com.example.PaymentService.service;

import com.example.PaymentService.model.entity.type.StatusType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RandomNumberServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RandomNumberService randomNumberService;

    @Test
    void determinePaymentStatus_ShouldReturnSuccess_WhenNumberIsEven() {
        ReflectionTestUtils.setField(randomNumberService, "randomNumberUrl", "https://test-url");
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("42");

        StatusType result = randomNumberService.determinePaymentStatus();

        assertThat(result).isEqualTo(StatusType.SUCCESS);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void determinePaymentStatus_ShouldReturnFailed_WhenNumberIsOdd() {
        ReflectionTestUtils.setField(randomNumberService, "randomNumberUrl", "https://test-url");
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("43");

        StatusType result = randomNumberService.determinePaymentStatus();

        assertThat(result).isEqualTo(StatusType.FAILED);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void determinePaymentStatus_ShouldReturnFailed_WhenExceptionOccurs() {
        ReflectionTestUtils.setField(randomNumberService, "randomNumberUrl", "https://test-url");
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("API error"));

        StatusType result = randomNumberService.determinePaymentStatus();

        assertThat(result).isEqualTo(StatusType.FAILED);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void determinePaymentStatus_ShouldReturnFailed_WhenResponseIsNull() {
        ReflectionTestUtils.setField(randomNumberService, "randomNumberUrl", "https://test-url");
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(null);

        StatusType result = randomNumberService.determinePaymentStatus();

        assertThat(result).isEqualTo(StatusType.FAILED);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void determinePaymentStatus_ShouldReturnFailed_WhenResponseIsEmpty() {
        ReflectionTestUtils.setField(randomNumberService, "randomNumberUrl", "https://test-url");
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("");

        StatusType result = randomNumberService.determinePaymentStatus();

        assertThat(result).isEqualTo(StatusType.FAILED);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }
}