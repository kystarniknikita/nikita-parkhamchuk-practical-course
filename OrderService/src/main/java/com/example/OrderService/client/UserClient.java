package com.example.OrderService.client;

import com.example.OrderService.model.dto.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userServiceClient", url = "${user.service.base-url}")
public interface UserClient {
    @GetMapping("/email")
    UserInfoResponse getByEmail(@RequestParam("email") String email);

    @GetMapping("/{id}")
    UserInfoResponse getById(@PathVariable("id") Long id);
}