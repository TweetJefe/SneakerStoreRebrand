package com.sneaker.store.products.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        // ЛОГ №1: Проверяем, что токен пришел
        System.out.println(">>> FILTER: Checking token...");

        if (jwtUtils.validateJwtToken(jwt)) {
            String userEmail = jwtUtils.getUserNameFromJwtToken(jwt);
            var authorities = jwtUtils.getAuthoritiesFromJwtToken(jwt);

            // ЛОГ №2: Самый важный! Что программа видит внутри токена?
            System.out.println(">>> FILTER SUCCESS: User: " + userEmail);
            System.out.println(">>> FILTER ROLES: " + authorities);

            // Создаем пользователя БЕЗ базы данных, чисто на основе токена
            UserDetails userDetails = new User(userEmail, "", authorities);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            System.out.println(">>> FILTER FAIL: Token is invalid");
        }

        filterChain.doFilter(request, response);
    }
}