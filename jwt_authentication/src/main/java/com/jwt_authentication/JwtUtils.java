package com.jwt_authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("JWT Verification Error: " + e.getMessage());
        }

        return false;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }


    public String getUserNameFromJwtToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<GrantedAuthority> getAuthoritiesFromJwtToken(String token) {
        Claims claims = getAllClaimsFromToken(token);

        List<String> roles = claims.get("roles", List.class);

        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}