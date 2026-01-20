package com.example.UserService.service;

import com.example.UserService.exception.user.UserAlreadyExistsException;
import com.example.UserService.exception.user.UserNotFoundException;
import com.example.UserService.mapper.UserMapper;
import com.example.UserService.model.dto.UserRequest;
import com.example.UserService.model.dto.UserResponse;
import com.example.UserService.model.entity.User;
import com.example.UserService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional
    @CacheEvict(allEntries = true)
    public UserResponse create(UserRequest dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException(dto.getEmail());
        });
        User user = userMapper.toEntity(dto);
        if (user.getCards() != null) {
            user.getCards().forEach(user::addCardInfo);
        }

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toDto(user);
    }

    public List<UserResponse> findAll() {
        return userMapper.toDtos(userRepository.findAll());
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return userMapper.toDto(user);
    }

    @Transactional
    @CachePut(key = "#id")
    public UserResponse update(Long id, UserRequest dto) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateUserFromDto(foundUser, dto);
        User saved = userRepository.save(foundUser);

        return userMapper.toDto(saved);
    }

    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);
    }
}
