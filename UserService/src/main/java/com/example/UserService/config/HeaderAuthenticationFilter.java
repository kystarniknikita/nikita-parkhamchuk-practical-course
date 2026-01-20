package com.example.UserService.config;

import com.example.UserService.model.dto.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@Slf4j
class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String userIdHeader = request.getHeader("X-User-Id");
        String userRoleHeader = request.getHeader("X-User-Role");

        log.info("Headers received - X-User-Id: {}, X-User-Role: {}", userIdHeader, userRoleHeader);

        if (userIdHeader != null && userRoleHeader != null) {
            try {
                Long userId = Long.parseLong(userIdHeader);
                UserPrincipal principal = new UserPrincipal(userId, userRoleHeader);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRoleHeader))
                        );

                log.info("Setting authentication with principal: {}", principal);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (NumberFormatException e) {
                log.warn("Invalid X-User-Id format: " + userIdHeader);
            }
        } else {
            log.warn("Missing authentication headers for path: {}", request.getRequestURI());
        }

        chain.doFilter(request, response);
    }
}