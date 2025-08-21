package org.example.miroom.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.miroom.config.JwtProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Component
@RequiredArgsConstructor

public class JwtUtil {

    private final JwtProperties jwtProperties;
    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));

    }

    public String creteToken(String email){
        return Jwts.builder()
                .setSubject("user")
                .claim("email",email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+jwtProperties.getExpiration()))
                .signWith(key)
                .compact();
    }
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 이메일 꺼내는 메서드
    public String getEmail(String token) {
        return parseClaims(token).get("email", String.class);
    }
}
