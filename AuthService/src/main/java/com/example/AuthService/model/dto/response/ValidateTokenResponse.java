package com.example.AuthService.model.dto.response;

import lombok.Data;

@Data
public class ValidateTokenResponse {
    private boolean valid;
    private String username;
    private String role;
}
