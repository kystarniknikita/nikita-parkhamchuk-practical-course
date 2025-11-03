package com.example.UserService.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.UserService.config.IntegrationTestConfig;
import com.example.UserService.model.dto.CardInfoRequest;
import com.example.UserService.model.dto.CardInfoResponse;
import com.example.UserService.model.dto.UserRequest;
import com.example.UserService.model.dto.UserResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Tag("integration")
@SpringBootTest
@Transactional
public class CardInfoServiceIntegrationTest extends IntegrationTestConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private CardInfoService cardInfoService;

    @Test
    void testAddCardToUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test");
        userRequest.setSurname("Test");
        userRequest.setEmail("Test@someemail.com");
        userRequest.setBirthDate(LocalDateTime.now().minusYears(20));
        UserResponse user = userService.create(userRequest);

        CardInfoRequest cardRequest = new CardInfoRequest();
        cardRequest.setNumber("1234567890123456");
        cardRequest.setUserId(user.getId());
        cardRequest.setExpirationDate(LocalDateTime.now().plusYears(10));
        cardRequest.setHolder(userRequest.getName());

        CardInfoResponse card = cardInfoService.create(cardRequest);

        assertThat(card.getId()).isNotNull();
        assertThat(card.getNumber()).isEqualTo("1234567890123456");
    }
}
