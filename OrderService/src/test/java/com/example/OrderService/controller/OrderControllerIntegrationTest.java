package com.example.OrderService.controller;

import com.example.OrderService.model.dto.CreateOrderRequest;
import com.example.OrderService.model.dto.OrderItemRequest;
import com.example.OrderService.model.entity.Item;
import com.example.OrderService.repository.ItemRepository;
import com.example.OrderService.service.OrderEventProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class OrderControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("order_test")
            .withUsername("test")
            .withPassword("test");

    static WireMockServer wireMockServer;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("user.service.base-url",
                () -> "http://localhost:" + wireMockServer.port());
    }

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ItemRepository itemRepository;

    @MockBean
    OrderEventProducer orderEventProducer;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();

        itemRepository.save(
                Item.builder()
                        .name("iPhone")
                        .price(new BigDecimal("1000.00"))
                        .build()
        );

        wireMockServer.stubFor(
                get(urlPathEqualTo("/email"))
                        .withQueryParam("email", equalTo("nikitakyst@gmail.com"))
                        .willReturn(okJson("""
                                {
                                  "id": 1,
                                  "name": "Nikita",
                                  "surname": "Kyst",
                                  "email": "nikitakyst@gmail.com"
                                }
                                """))
        );

        wireMockServer.stubFor(
                get(urlPathEqualTo("/1"))
                        .willReturn(okJson("""
                                {
                                  "id": 1,
                                  "name": "Nikita",
                                  "surname": "Kyst",
                                  "email": "nikitakyst@gmail.com"
                                }
                                """))
        );
    }

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        Item item = itemRepository.findAll().get(0);

        CreateOrderRequest request = new CreateOrderRequest(
                "nikitakyst@gmail.com",
                List.of(new OrderItemRequest(item.getId(), 2))
        );

        mockMvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.items[0].itemId").value(item.getId()))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.user.email").value("nikitakyst@gmail.com"));
    }
}
