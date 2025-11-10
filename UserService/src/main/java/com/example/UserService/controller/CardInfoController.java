package com.example.UserService.controller;

import com.example.UserService.model.dto.CardInfoRequest;
import com.example.UserService.model.dto.CardInfoResponse;
import com.example.UserService.service.CardInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardInfoController {

    private final CardInfoService cardInfoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CardInfoResponse>> getAll() {
        return ResponseEntity.ok(cardInfoService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CardInfoResponse> create(@RequestBody @Valid CardInfoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardInfoService.create(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @cardInfoService.isCardOwner(#id, authentication.name)")
    public ResponseEntity<CardInfoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cardInfoService.findCardById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @cardInfoService.isCardOwner(#id, authentication.name)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardInfoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}