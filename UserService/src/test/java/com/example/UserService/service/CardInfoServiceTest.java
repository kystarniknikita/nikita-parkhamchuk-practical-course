package com.example.UserService.service;

import com.example.UserService.exception.card.CardNotFoundException;
import com.example.UserService.exception.card.CardNumberAlreadyExistsException;
import com.example.UserService.exception.user.UserNotFoundException;
import com.example.UserService.mapper.CardInfoMapper;
import com.example.UserService.model.dto.CardInfoRequest;
import com.example.UserService.model.dto.CardInfoResponse;
import com.example.UserService.model.entity.CardInfo;
import com.example.UserService.model.entity.User;
import com.example.UserService.repository.CardInfoRepository;
import com.example.UserService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardInfoServiceTest {

    @Mock
    private CardInfoRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardInfoMapper cardMapper;

    @InjectMocks
    private CardInfoService cardInfoService;

    private User user;
    private CardInfo card;
    private CardInfoRequest request;
    private CardInfoResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .name("Name")
                .surname("Surname")
                .email("name@gmail.com")
                .birthDate(LocalDateTime.now().minusYears(20))
                .build();

        card = CardInfo.builder()
                .id(10L)
                .number("1234567812345678")
                .holder("SOME CARD")
                .expirationDate(LocalDateTime.now().plusYears(2))
                .user(user)
                .build();

        request = new CardInfoRequest();
        request.setUserId(1L);
        request.setNumber("1234567812345678");
        request.setHolder("SOME CARD");
        request.setExpirationDate(LocalDateTime.now().plusYears(2));

        response = new CardInfoResponse();
        response.setId(10L);
        response.setNumber(card.getNumber());
        response.setHolder(card.getHolder());
        response.setExpirationDate(card.getExpirationDate());
        response.setUserId(1L);
    }

    @Test
    void create_shouldSave_whenUserFoundAndCardNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.findByNumber("1234567812345678")).thenReturn(Optional.empty());
        when(cardMapper.toEntity(request)).thenReturn(card);
        when(cardRepository.save(any(CardInfo.class))).thenReturn(card);
        when(cardMapper.toDto(card)).thenReturn(response);

        CardInfoResponse result = cardInfoService.create(request);

        assertNotNull(result);
        assertEquals(response.getNumber(), result.getNumber());
        verify(cardRepository, times(1)).save(any(CardInfo.class));
    }

    @Test
    void create_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cardInfoService.create(request));
        verify(cardRepository, never()).save(any());
    }

    @Test
    void create_shouldThrow_whenCardNumberAlreadyExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.findByNumber("1234567812345678")).thenReturn(Optional.of(card));

        assertThrows(CardNumberAlreadyExistsException.class,
                () -> cardInfoService.create(request));

        verify(cardRepository, never()).save(any());
    }

    @Test
    void findCardById_shouldReturn_whenExists() {
        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));
        when(cardMapper.toDto(card)).thenReturn(response);

        CardInfoResponse result = cardInfoService.findCardById(10L);

        assertNotNull(result);
        assertEquals("1234567812345678", result.getNumber());
        verify(cardRepository).findById(10L);
    }

    @Test
    void findCardById_shouldThrow_whenNotFound() {
        when(cardRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardInfoService.findCardById(10L));
    }

    @Test
    void findAll_shouldReturnList() {
        when(cardRepository.findAll()).thenReturn(List.of(card));
        when(cardMapper.toDtos(List.of(card))).thenReturn(List.of(response));

        List<CardInfoResponse> result = cardInfoService.findAll();

        assertEquals(1, result.size());
        assertEquals("1234567812345678", result.get(0).getNumber());
    }

    @Test
    void deleteById_shouldRemoveCard() {
        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));

        cardInfoService.deleteById(10L);

        verify(cardRepository).delete(card);
    }

    @Test
    void deleteById_shouldThrow_whenCardNotFound() {
        when(cardRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardInfoService.deleteById(10L));
        verify(cardRepository, never()).delete(any());
    }
}
