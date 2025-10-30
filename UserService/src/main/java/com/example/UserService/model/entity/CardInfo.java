package com.example.UserService.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16)
    private String number;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false, length = 100)
    private String holder;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
