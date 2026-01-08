//package com.example.ApiGateway.config;
//
//
//import com.example.ApiGateway.filter.JwtAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class GatewayConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//
//    @Value("${services.auth-service.url}")
//    private String authServiceUrl;
//
//    @Value("${services.user-service.url}")
//    private String userServiceUrl;
//
//    @Value("${services.order-service.url}")
//    private String orderServiceUrl;
//
//    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("auth-login", r -> r
//                        .path("/auth/login")
//                        .uri(authServiceUrl))
//                .route("auth-register", r -> r
//                        .path("/auth/register")
//                        .uri(authServiceUrl))
//                .route("auth-refresh", r -> r
//                        .path("/auth/refresh")
//                        .uri(authServiceUrl))
//
//                .route("user-service", r -> r
//                        .path("/users/")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri(userServiceUrl))
//
//                .route("order-service", r -> r
//                        .path("/orders/", "/api/items/**")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri(orderServiceUrl))
//
//                .route("auth-protected", r -> r
//                        .path("/auth/profile", "/auth/logout")
//                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
//                        .uri(authServiceUrl))
//
//                .build();
//    }
//}
