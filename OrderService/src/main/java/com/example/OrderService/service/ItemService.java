package com.example.OrderService.service;

import com.example.OrderService.exception.ResourceNotFoundException;
import com.example.OrderService.model.entity.Item;
import com.example.OrderService.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item getById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("item", id));
    }
}
