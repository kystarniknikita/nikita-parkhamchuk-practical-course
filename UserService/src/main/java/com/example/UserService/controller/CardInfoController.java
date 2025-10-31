package com.example.UserService.controller;

import com.example.UserService.model.dto.CardInfoRequest;
import com.example.UserService.model.dto.CardInfoResponse;
import com.example.UserService.service.CardInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardInfoController {

    private final CardInfoService cardInfoService;

    @GetMapping
    public ResponseEntity<List<CardInfoResponse>> getAll() {
        return ResponseEntity.ok(cardInfoService.findAll());
    }

    @PostMapping
    public ResponseEntity<CardInfoResponse> create(@RequestBody @Valid CardInfoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardInfoService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardInfoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cardInfoService.findCardById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardInfoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
