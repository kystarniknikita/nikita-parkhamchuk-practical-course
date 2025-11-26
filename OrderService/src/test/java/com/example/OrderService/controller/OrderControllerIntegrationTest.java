package com.example.OrderService.controller;

import com.example.OrderService.model.dto.CreateOrderRequest;
import com.example.OrderService.model.dto.OrderItemRequest;
import com.example.OrderService.model.dto.OrderResponse;
import com.example.OrderService.model.entity.Item;
import com.example.OrderService.repository.ItemRepository;
import com.example.OrderService.repository.OrderRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@TestPropertySource(properties = {
        "user.service.base-url=http://localhost:8080/api/v1/users",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
class OrderControllerIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private Item testItem;
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);

        testItem = new Item();
        testItem.setName("Test Item");
        testItem.setPrice(BigDecimal.valueOf(10.0));
        testItem = itemRepository.save(testItem);

        stubFor(get(urlPathEqualTo("/api/v1/users/email"))
                .withQueryParam("email", equalTo("some@email.com"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": 1,
                                    "name": "Test",
                                    "surname": "User", 
                                    "email": "some@email.com"
                                }
                                """)));

        stubFor(get(urlPathEqualTo("/api/v1/users/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": 1,
                                    "name": "Test",
                                    "surname": "User", 
                                    "email": "some@email.com"
                                }
                                """)));
    }

    @AfterEach
    void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
        orderRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void testCreateOrder() {
        var createRequest = new CreateOrderRequest("some@email.com",
                List.of(new OrderItemRequest(testItem.getId(), 2)));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateOrderRequest> requestEntity = new HttpEntity<>(createRequest, headers);

        var response = restTemplate.postForEntity("/api/v1/orders", requestEntity, OrderResponse.class);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUser().getEmail()).isEqualTo("some@email.com");
        assertThat(response.getBody().getItems()).hasSize(1);

        verify(getRequestedFor(urlPathEqualTo("/api/v1/users/email"))
                .withQueryParam("email", equalTo("some@email.com")));
    }
}