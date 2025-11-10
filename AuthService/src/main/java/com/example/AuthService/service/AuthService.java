package com.example.AuthService.service;

import com.example.AuthService.model.dto.request.LoginRequest;
import com.example.AuthService.model.dto.request.RefreshTokenRequest;
import com.example.AuthService.model.dto.request.UserRequest;
import com.example.AuthService.model.dto.request.ValidateTokenRequest;
import com.example.AuthService.model.dto.response.TokenResponse;
import com.example.AuthService.model.dto.response.ValidateTokenResponse;
import com.example.AuthService.model.entity.RoleType;
import com.example.AuthService.model.entity.User;
import com.example.AuthService.repository.UserRepository;
import com.example.AuthService.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtTokenUtil.validateToken(refreshToken)) {
            String username = jwtTokenUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String newAccessToken = jwtTokenUtil.generateToken(userDetails);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            return new TokenResponse(newAccessToken, newRefreshToken);
        }
        throw new RuntimeException("Invalid refresh token");
    }

    public ValidateTokenResponse validateToken(ValidateTokenRequest request) {
        try {
            if (jwtTokenUtil.validateToken(request.getToken())) {
                String username = jwtTokenUtil.extractUsername(request.getToken());
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                ValidateTokenResponse response = new ValidateTokenResponse();
                response.setValid(true);
                response.setUsername(username);
                response.setRole(userDetails.getAuthorities().iterator().next().getAuthority());
                return response;
            }
        } catch (Exception e) {
            log.error("Error validate token", e);
        }

        ValidateTokenResponse response = new ValidateTokenResponse();
        response.setValid(false);
        return response;
    }

    public void register(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(RoleType.USER);
        userRepository.save(user);
    }
}
