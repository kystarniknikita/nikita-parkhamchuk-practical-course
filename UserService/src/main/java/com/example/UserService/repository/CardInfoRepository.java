package com.example.UserService.repository;

import com.example.UserService.model.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {
    Optional<CardInfo> findByNumber(String number);
}
