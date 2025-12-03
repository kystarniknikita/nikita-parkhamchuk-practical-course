package com.example.AuthService.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;


    @NotNull
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}