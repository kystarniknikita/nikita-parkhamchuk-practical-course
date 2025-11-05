package com.example.UserService.service;

import com.example.UserService.exception.user.UserAlreadyExistsException;
import com.example.UserService.exception.user.UserNotFoundException;
import com.example.UserService.mapper.UserMapper;
import com.example.UserService.model.dto.UserRequest;
import com.example.UserService.model.dto.UserResponse;
import com.example.UserService.model.entity.User;
import com.example.UserService.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRequest request;
    private User user;
    private UserResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new UserRequest();
        request.setName("Name");
        request.setSurname("Surname");
        request.setEmail("name@gmail.com");
        request.setBirthDate(LocalDateTime.now().minusYears(20));

        user = User.builder()
                .id(1L)
                .name("Name")
                .surname("Surname")
                .email("name@gmail.com")
                .birthDate(LocalDateTime.now().minusYears(20))
                .build();

        response = new UserResponse();
        response.setId(1L);
        response.setName("Name");
        response.setSurname("Surname");
        response.setEmail("name@gmail.com");
        response.setBirthDate(LocalDateTime.now().minusYears(25));
    }

    @Test
    void create_shouldSaveUser_whenEmailNotExists() {
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(response);

        UserResponse result = userService.create(request);

        assertNotNull(result);
        assertEquals(request.getEmail(), result.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void create_shouldThrow_whenEmailExists() {
        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(request));
    }

    @Test
    void findById_shouldReturnUser_whenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(response);

        UserResponse result = userService.findById(1L);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void findAll_shouldReturnList() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDtos(users)).thenReturn(List.of(response));

        List<UserResponse> result = userService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void findByEmail_shouldReturn_whenFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(response);

        UserResponse res = userService.findByEmail(user.getEmail());
        assertEquals(user.getEmail(), res.getEmail());
    }

    @Test
    void findByEmail_shouldThrow_whenNotFound() {
        when(userRepository.findByEmail("x@x.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("x@x.com"));
    }

    @Test
    void update_shouldModifyExistingUser() {
        request.setName("New");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doAnswer(inv -> {
            user.setName(request.getName());
            return null;
        }).when(userMapper).updateUserFromDto(user, request);

        when(userRepository.save(user)).thenReturn(user);
        UserResponse updatedResp = response;
        updatedResp.setName("New");
        when(userMapper.toDto(user)).thenReturn(updatedResp);

        UserResponse result = userService.update(1L, request);
        assertEquals("New", result.getName());
    }

    @Test
    void update_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.update(1L, request));
    }

    @Test
    void deleteById_shouldRemove_whenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteById_shouldThrow_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteById(99L));
    }
}