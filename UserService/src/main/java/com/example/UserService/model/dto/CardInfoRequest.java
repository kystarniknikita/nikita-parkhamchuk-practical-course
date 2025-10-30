package com.example.UserService.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class CardInfoRequest {
    @NotBlank
    @Length(min = 16, max = 16)
    private String number;

    @NotNull
    @Future
    private LocalDateTime expirationDate;

    @NotBlank
    @Length(min = 2, max = 100)
    private String holder;

    private Long userId;

}
