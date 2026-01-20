package com.example.ApiGateway.dto;

import com.example.ApiGateway.dto.type.RoleType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationRequestDto {
    private String name;
    private String surname;
    private LocalDateTime birthDate;
    private String email;

    private String username;
    private String password;
    private RoleType role = RoleType.USER;
}