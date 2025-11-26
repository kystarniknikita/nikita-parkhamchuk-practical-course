package com.example.OrderService.service;

import com.example.OrderService.client.UserClient;
import com.example.OrderService.model.dto.UserInfoResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;

    @CircuitBreaker(name = "userServiceClient", fallbackMethod = "fallbackUser")
    public UserInfoResponse getByEmail(String email){
        return userClient.getByEmail(email);
    }

    @CircuitBreaker(name = "userServiceClient", fallbackMethod = "fallbackUserById")
    public UserInfoResponse getById(Long id){
        return userClient.getById(id);
    }

    public UserInfoResponse fallbackUser(String email, Throwable t) {
        return new UserInfoResponse(null, "unknown", "unknown", email);
    }
    
    public UserInfoResponse fallbackUserById(Long id, Throwable t) {
        return new UserInfoResponse(id, "unknown", "unknown", "unknown");
    }
}
