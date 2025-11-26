package com.example.OrderService.service;

import com.example.OrderService.exception.ResourceNotFoundException;
import com.example.OrderService.mapper.OrderMapper;
import com.example.OrderService.model.dto.*;
import com.example.OrderService.model.entity.Item;
import com.example.OrderService.model.entity.Order;
import com.example.OrderService.model.entity.OrderItem;
import com.example.OrderService.model.entity.OrderStatus;
import com.example.OrderService.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemService itemService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_success() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setUserEmail("u@example.com");
        OrderItemRequest oir = new OrderItemRequest();
        oir.setItemId(10L);
        oir.setQuantity(2);
        req.setOrderItems(List.of(oir));

        UserInfoResponse user = new UserInfoResponse(5L,"User","1", "u@example.com");
        when(userService.getByEmail("u@example.com")).thenReturn(user);

        Order mapped = new Order();
        mapped.setStatus(OrderStatus.PENDING);
        when(orderMapper.toEntity(req)).thenReturn(mapped);

        Item item = new Item();
        item.setId(10L);
        item.setName("ItemA");
        item.setPrice(new BigDecimal("12.34"));

        when(itemService.getById(10L)).thenReturn(item);

        Order saved = new Order();
        saved.setId(100L);
        saved.setUserId(5L);
        saved.setStatus(OrderStatus.PENDING);
        OrderItem oi = new OrderItem();
        oi.setId(1L);
        oi.setItem(item);
        oi.setQuantity(2);
        oi.setOrder(saved);
        saved.setItems(List.of(oi));

        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        OrderResponse resp = new OrderResponse();
        resp.setId(100L);
        when(orderMapper.toResponse(saved)).thenReturn(resp);

        OrderResponse result = orderService.create(req);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());
        Order toSave = captor.getValue();
        assertEquals(5L, toSave.getUserId());
        assertFalse(toSave.getItems().isEmpty());
        verify(itemService).getById(10L);
    }

    @Test
    void findById_found() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(2L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        UserInfoResponse user = new UserInfoResponse(2L,"A", "B", "a@a");
        when(userService.getById(2L)).thenReturn(user);

        OrderResponse resp = new OrderResponse();
        resp.setId(1L);
        when(orderMapper.toResponse(order)).thenReturn(resp);

        OrderResponse found = orderService.findById(1L);
        assertEquals(1L, found.getId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void findById_notFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> orderService.findById(999L));
        assertTrue(ex.getMessage().contains("Order not found"));
    }

    @Test
    void findByIds() {
        Order o1 = new Order(); o1.setId(1L);
        when(orderRepository.findAllByIdIn(List.of(1L,2L))).thenReturn(List.of(o1));
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        when(orderMapper.toResponse(o1)).thenReturn(orderResponse);
        List<OrderResponse> res = orderService.findByIds(List.of(1L,2L));
        assertEquals(1, res.size());
        verify(orderRepository).findAllByIdIn(anyList());
    }

    @Test
    void findByStatuses() {
        Order o1 = new Order(); o1.setId(1L);
        when(orderRepository.findByStatusIn(List.of(OrderStatus.PENDING))).thenReturn(List.of(o1));
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(1L);
        when(orderMapper.toResponse(o1)).thenReturn(orderResponse);
        List<OrderResponse> res = orderService.findByStatuses(List.of(OrderStatus.PENDING));
        assertEquals(1, res.size());
    }

    @Test
    void updateById_success() {
        Order existing = new Order();
        existing.setId(10L);
        existing.setUserId(2L);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(existing));

        UpdateOrderRequest req = new UpdateOrderRequest();
        req.setUserEmail("z@z");
        req.setStatus(OrderStatus.CONFIRMED);
        OrderItemRequest oir = new OrderItemRequest();
        oir.setItemId(20L);
        oir.setQuantity(3);
        req.setOrderItems(List.of(oir));

        UserInfoResponse user = new UserInfoResponse(7L, "Z", "A", "z@z");
        when(userService.getByEmail("z@z")).thenReturn(user);

        Item item = new Item();
        item.setId(20L);
        when(itemService.getById(20L)).thenReturn(item);

        Order saved = new Order();
        saved.setId(10L);
        saved.setUserId(7L);
        saved.setStatus(OrderStatus.CONFIRMED);
        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(10L);
        when(orderMapper.toResponse(saved)).thenReturn(orderResponse);

        OrderResponse resp = orderService.updateById(10L, req);
        assertEquals(10L, resp.getId());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void updateById_notFound() {
        when(orderRepository.findById(55L)).thenReturn(Optional.empty());
        UpdateOrderRequest req = new UpdateOrderRequest();
        req.setUserEmail("a@a");
        req.setOrderItems(List.of());
        assertThrows(ResourceNotFoundException.class, () -> orderService.updateById(55L, req));
    }

    @Test
    void deleteById_success() {
        Order ex = new Order();
        ex.setId(99L);
        when(orderRepository.findById(99L)).thenReturn(Optional.of(ex));
        doNothing().when(orderRepository).delete(ex);
        orderService.deleteById(99L);
        verify(orderRepository).delete(ex);
    }

    @Test
    void deleteById_notFound() {
        when(orderRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteById(123L));
    }
}