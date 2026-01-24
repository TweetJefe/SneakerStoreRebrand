package com.sneaker.store.users.controller;

import com.sneaker.store.users.dto.JwtRequest;
import com.sneaker.store.users.dto.JwtResponse;
import com.sneaker.store.users.security.JwtUtils; // <--- ПРОВЕРЬТЕ ИМЯ ЭТОГО КЛАССА У СЕБЯ
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils; // Ваш класс генерации токена

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails user = userDetailsService.loadUserByUsername(request.email());

        String token = jwtUtils.generateToken(user);

        return ResponseEntity.ok(new JwtResponse(token));
    }
}