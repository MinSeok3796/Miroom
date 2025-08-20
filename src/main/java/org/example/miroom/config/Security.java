package org.example.miroom.config;

import org.example.miroom.filter.JwtAuthenticationFilter;
import org.example.miroom.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Security {

    private final JwtTokenProvider jwtTokenProvider;

    public Security(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenProvider);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers( //토큰 필요없이
                                "/api/auth/login",
                                "/api/users/signup"
                        ).permitAll()

                        .requestMatchers( //권한(토큰) 필요
                                "/api/auth/logout",
                                "/api/users/withdraw",
                                "/api/friend-requests",
                                "/api/friend-requests/**",
                                "/api/friends",
                                "/api/friends/**"
                        ).authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}