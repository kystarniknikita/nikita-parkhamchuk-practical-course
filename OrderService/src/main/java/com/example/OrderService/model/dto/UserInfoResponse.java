package com.example.OrderService.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
}
