package com.example.UserService.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "birth_date", nullable = false)
    private LocalDateTime birthDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Builder.Default
    @OneToMany(
            mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER
    )
    private List<CardInfo> cards = new ArrayList<>();

    public void addCardInfo(CardInfo card) {
        cards.add(card);
        card.setUser(this);
    }

    public void removeCardInfo(CardInfo card) {
        cards.remove(card);
        card.setUser(null);
    }
}

