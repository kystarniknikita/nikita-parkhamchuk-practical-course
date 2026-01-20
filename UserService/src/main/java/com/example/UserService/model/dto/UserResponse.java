package com.example.UserService.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserResponse implements Serializable {
    private Long id;

    private String name;

    private String surname;

    private LocalDateTime birthDate;

    private String email;

    private List<CardInfoResponse> cards = new ArrayList<>();
}
