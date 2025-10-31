package com.example.UserService.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserRequest {
    @NotBlank
    @Length(min = 2, max = 20)
    private String name;

    @NotBlank
    @Length(min = 2, max = 20)
    private String surname;

    @Past
    @NotNull
    private LocalDateTime birthDate;

    @NotBlank
    @Email
    private String email;

    private List<CardInfoRequest> cards = new ArrayList<>();
}
