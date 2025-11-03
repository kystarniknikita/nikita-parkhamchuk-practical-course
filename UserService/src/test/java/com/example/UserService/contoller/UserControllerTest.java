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
import java.util.Collections;
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

    @Test
    void testGetUserByIdSuccess() throws Exception {
        UserResponse user = new UserResponse();
        user.setId(1L);
        user.setName("Nikita");
        user.setSurname("Kyst");
        user.setEmail("nikita@example.com");

        Mockito.when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nikita"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        Mockito.when(userService.findById(999L)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("Updated");
        request.setSurname("User");
        request.setBirthDate(LocalDateTime.now().minusYears(1));
        request.setEmail("updated@example.com");

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("Updated");
        response.setSurname("User");
        response.setBirthDate(request.getBirthDate());
        response.setEmail(request.getEmail());

        Mockito.when(userService.update(Mockito.eq(1L), Mockito.any(UserRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testGetUserByEmailSuccess() throws Exception {
        UserResponse user = new UserResponse();
        user.setId(1L);
        user.setName("Nikita");
        user.setSurname("Kyst");
        user.setEmail("nikita@example.com");

        Mockito.when(userService.findByEmail("nikita@example.com")).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/email")
                        .param("email", "nikita@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nikita"));
    }

    @Test
    void testGetUserByEmailNotFound() throws Exception {
        Mockito.when(userService.findByEmail("unknown@example.com"))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/v1/users/email")
                        .param("email", "unknown@example.com"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testCreateUserValidationFail() throws Exception {
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setName("");
        invalidRequest.setSurname("S");
        invalidRequest.setEmail("invalid-email");
        invalidRequest.setBirthDate(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("Name");
        request.setSurname("Surname");
        request.setBirthDate(LocalDateTime.now().minusYears(20));
        request.setEmail("test@example.com");

        Mockito.when(userService.update(Mockito.eq(999L), Mockito.any(UserRequest.class)))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(put("/api/v1/users/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testGetAllUsersEmpty() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        Mockito.doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("User not found")).when(userService).deleteById(999L);

        mockMvc.perform(delete("/api/v1/users/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}
