package com.sneaker.store.users.security;

import com.sneaker.store.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <--- ВАЖНО

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    @Transactional // <--- ТЕПЕРЬ ЭТО БЕЗОПАСНО И НУЖНО
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("========== AUTH DEBUG START ==========");
        System.out.println("Login attempt for: " + email);

        var user = repository.findUserByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("!!! User NOT FOUND in DB !!!");
                    return new UsernameNotFoundException("User not found");
                });

        System.out.println("User found ID: " + user.getId());
        System.out.println("DB Password Hash: " + user.getPassword());

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Загружаем роли (внутри транзакции это не упадет)
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {
                System.out.println("Role loaded: " + role);
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            });
        }

        System.out.println("========== AUTH DEBUG END ==========");

        return new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}