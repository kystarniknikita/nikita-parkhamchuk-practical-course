package com.example.UserService.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CardInfoResponse implements Serializable {
    private Long id;

    private String number;

    private LocalDateTime expirationDate;

    private String holder;

    private Long userId;
}
