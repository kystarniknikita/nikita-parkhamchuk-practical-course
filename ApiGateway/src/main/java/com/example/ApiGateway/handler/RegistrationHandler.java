package com.example.ApiGateway.handler;

import com.example.ApiGateway.dto.RegistrationRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class RegistrationHandler {

    private final WebClient webClient;
    private final String userServiceUrl;
    private final String authServiceUrl;

    public RegistrationHandler(WebClient.Builder webClientBuilder,
                               @Value("${urls.user-service}") String userServiceUrl,
                               @Value("${urls.auth-service}") String authServiceUrl) {
        this.webClient = webClientBuilder.build();
        this.userServiceUrl = userServiceUrl;
        this.authServiceUrl = authServiceUrl;
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        System.out.println("register");
        return request.bodyToMono(RegistrationRequestDto.class)
                .flatMap(dto -> {

                    return createUserInUserService(dto)
                            .flatMap(userResponse -> {
                                Long userId = extractId(userResponse);
                                log.info("User created in User Service with ID: {}", userId);
                                return createCredentialsInAuthService(dto)
                                        .then(ServerResponse.status(HttpStatus.CREATED).bodyValue("User registered successfully"))

                                        .onErrorResume(e -> {
                                            log.error("Failed to create credentials. Rolling back user creation for ID: {}", userId);
                                            return rollbackUserCreation(userId)
                                                    .then(Mono.error(new RuntimeException("Registration failed, rollback executed: " + e.getMessage())));
                                        });
                            });
                })
                .onErrorResume(e -> ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()));
    }

    private Mono<Map> createUserInUserService(RegistrationRequestDto dto) {
        System.out.println("maaaaaaap");
        Map<String, Object> userRequest = Map.of(
                "name", dto.getName(),
                "surname", dto.getSurname(),
                "birthDate", dto.getBirthDate().toString(),
                "email", dto.getEmail()
        );

        System.out.println(userRequest);
        System.out.println(userServiceUrl);
        return webClient.post()
                .uri(userServiceUrl + "/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .retrieve()
                .bodyToMono(Map.class);
    }

    private Mono<Void> createCredentialsInAuthService(RegistrationRequestDto dto) {
        Map<String, Object> authRequest = Map.of(
                "username", dto.getUsername(),
                "password", dto.getPassword(),
                "email", dto.getEmail(),
                "role", dto.getRole()
        );

        return webClient.post()
                .uri(authServiceUrl + "/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequest)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private Mono<Void> rollbackUserCreation(Long userId) {
        return webClient.delete()
                .uri(userServiceUrl + "/api/v1/users/" + userId)
                .retrieve()
                .bodyToMono(Void.class);
    }

    private Long extractId(Map userResponse) {
        Object idObj = userResponse.get("id");
        if (idObj instanceof Integer) {
            return ((Integer) idObj).longValue();
        } else if (idObj instanceof Long) {
            return (Long) idObj;
        }
        throw new IllegalArgumentException("Invalid ID format from User Service");
    }
}