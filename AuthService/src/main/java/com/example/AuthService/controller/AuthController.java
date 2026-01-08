package com.example.AuthService.controller;

import com.example.AuthService.model.dto.request.LoginRequest;
import com.example.AuthService.model.dto.request.RefreshTokenRequest;
import com.example.AuthService.model.dto.request.UserRequest;
import com.example.AuthService.model.dto.request.ValidateTokenRequest;
import com.example.AuthService.model.dto.response.TokenResponse;
import com.example.AuthService.model.dto.response.ValidateTokenResponse;
import com.example.AuthService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(@Valid @RequestBody ValidateTokenRequest request) {
        return ResponseEntity.ok(authService.validateToken(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest request) {
        System.out.println("Received username: " + request.getUsername());
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }
}
