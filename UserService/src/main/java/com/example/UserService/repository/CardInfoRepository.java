package com.example.UserService.repository;

import com.example.UserService.model.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {
}
