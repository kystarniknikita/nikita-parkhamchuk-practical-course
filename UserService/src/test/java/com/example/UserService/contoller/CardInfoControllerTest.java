package com.example.UserService.contoller;

import com.example.UserService.controller.CardInfoController;
import com.example.UserService.model.dto.CardInfoRequest;
import com.example.UserService.model.dto.CardInfoResponse;
import com.example.UserService.service.CardInfoService;
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

@WebMvcTest(CardInfoController.class)
class CardInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardInfoService cardInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCard() throws Exception {
        CardInfoRequest request = new CardInfoRequest();
        request.setNumber("1234567890123456");
        request.setHolder("Nikita Kyst");
        request.setExpirationDate(LocalDateTime.now().plusYears(2));
        request.setUserId(1L);

        CardInfoResponse response = new CardInfoResponse();
        response.setId(1L);
        response.setNumber(request.getNumber());
        response.setHolder(request.getHolder());
        response.setExpirationDate(request.getExpirationDate());
        response.setUserId(request.getUserId());

        Mockito.when(cardInfoService.create(Mockito.any(CardInfoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("1234567890123456"));
    }

    @Test
    void testGetAllCards() throws Exception {
        CardInfoResponse c1 = new CardInfoResponse();
        c1.setId(1L);
        c1.setNumber("1111222233334444");

        CardInfoResponse c2 = new CardInfoResponse();
        c2.setId(2L);
        c2.setNumber("5555666677778888");

        Mockito.when(cardInfoService.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
