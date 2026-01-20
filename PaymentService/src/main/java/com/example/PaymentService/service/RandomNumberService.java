package com.example.PaymentService.service;

import com.example.PaymentService.model.entity.type.StatusType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RandomNumberService {

    private final RestTemplate restTemplate;

    @Value("${external.random-number.url:https://www.random.org/integers/?num=1&min=1&max=1000&col=1&base=10&format=plain&rnd=new}")
    private String randomNumberUrl;

    public StatusType determinePaymentStatus() {
        try {
            String response = restTemplate.getForObject(randomNumberUrl, String.class);
            Integer randomNumber = Integer.parseInt(response.trim());

            log.info("Generated random number: {}", randomNumber);

            return (randomNumber % 2 == 0)
                    ? StatusType.SUCCESS
                    : StatusType.FAILED;

        } catch (Exception e) {
            log.error("Error calling external random number API: {}", e.getMessage());
            return StatusType.FAILED;
        }
    }
}