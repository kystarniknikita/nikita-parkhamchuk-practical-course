package com.example.UserService.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserResponse implements Serializable{
    private Long id;

    private String name;

    private String surname;

    private LocalDateTime birthDate;

    private String email;

    private List<CardInfoResponse> cards = new ArrayList<>();
}
