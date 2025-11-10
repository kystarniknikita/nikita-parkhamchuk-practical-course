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
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "cards")
public class CardInfoService {

    private final CardInfoMapper cardMapper;

    private final CardInfoRepository cardRepository;

    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(allEntries = true)
    public CardInfoResponse create(CardInfoRequest dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        cardRepository.findByNumber(dto.getNumber()).ifPresent(existing -> {
            throw new CardNumberAlreadyExistsException(dto.getNumber());
        });

        CardInfo cardInfo = cardMapper.toEntity(dto);

        user.addCardInfo(cardInfo);

        CardInfo savedCard = cardRepository.save(cardInfo);
        return cardMapper.toDto(savedCard);
    }

    @Cacheable(key = "#id")
    public CardInfoResponse findCardById(Long id) {
        CardInfo card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));
        return cardMapper.toDto(card);
    }

    public List<CardInfoResponse> findAll() {
        return cardMapper.toDtos(cardRepository.findAll());
    }

    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        CardInfo card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        User cardUser = card.getUser();
        cardUser.removeCardInfo(card);

        cardRepository.delete(card);
    }

    public boolean isCardOwner(Long cardId, String email) {
        return cardRepository.findById(cardId)
                .map(card -> card.getUser().getEmail().equals(email))
                .orElse(false);
    }
}
