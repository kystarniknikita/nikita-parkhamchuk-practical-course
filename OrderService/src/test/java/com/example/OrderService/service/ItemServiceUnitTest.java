package com.example.OrderService.service;

import com.example.OrderService.exception.ResourceNotFoundException;
import com.example.OrderService.model.entity.Item;
import com.example.OrderService.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceUnitTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_found() {
        Item item = new Item();
        item.setId(12L);
        item.setName("X");
        when(itemRepository.findById(12L)).thenReturn(Optional.of(item));
        Item res = itemService.getById(12L);
        assertEquals(12L, res.getId());
    }

    @Test
    void getById_notFound() {
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> itemService.getById(999L));
    }
}
