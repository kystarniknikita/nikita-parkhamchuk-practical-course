package com.example.UserService.contoller;

import com.example.UserService.controller.UserController;
import com.example.UserService.model.dto.UserRequest;
import com.example.UserService.model.dto.UserResponse;
import com.example.UserService.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("Nikita");
        request.setSurname("Kyst");
        request.setBirthDate(LocalDateTime.now().minusYears(19));
        request.setEmail("nikita@example.com");

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("Nikita");
        response.setSurname("Kyst");
        response.setBirthDate(request.getBirthDate());
        response.setEmail(request.getEmail());

        Mockito.when(userService.create(Mockito.any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("nikita@example.com"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserResponse u1 = new UserResponse();
        u1.setId(1L);
        u1.setEmail("a@example.com");

        UserResponse u2 = new UserResponse();
        u2.setId(2L);
        u2.setEmail("b@example.com");

        Mockito.when(userService.findAll()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
