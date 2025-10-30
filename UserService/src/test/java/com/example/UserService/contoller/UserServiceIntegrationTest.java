package com.example.UserService.contoller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.UserService.config.IntegrationTestConfig;
import com.example.UserService.exception.user.UserAlreadyExistsException;
import com.example.UserService.model.dto.UserRequest;
import com.example.UserService.model.dto.UserResponse;
import com.example.UserService.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest extends IntegrationTestConfig {

    @Autowired
    private UserService userService;

    @Test
    void testCreateAndFindUser() {
        UserRequest request = new UserRequest();
        request.setName("Test");
        request.setSurname("Test");
        request.setEmail("test@someemail.com");
        request.setBirthDate(LocalDateTime.now().minusYears(20));

        UserResponse created = userService.create(request);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Test");

        UserResponse found = userService.findById(created.getId());
        assertThat(found.getEmail()).isEqualTo("test@someemail.com");
    }

    @Test
    void testCreateDuplicateEmailThrowsException() {
        UserRequest request = new UserRequest();
        request.setEmail("test@someemail.com");
        request.setName("New");
        request.setSurname("Test");
        request.setBirthDate(LocalDateTime.now().minusYears(20));

        userService.create(request);

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));
    }
}
