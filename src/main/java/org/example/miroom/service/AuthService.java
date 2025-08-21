package org.example.miroom.service;

import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.LoginRequest;
import org.example.miroom.dto.LoginResponse;
import org.example.miroom.entity.User;
import org.example.miroom.repository.RefreshTokenRepository;
import org.example.miroom.repository.UserRepository;
import org.example.miroom.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
        return new LoginResponse(user, accessToken, refreshToken);
    }

    public void logout(String accessToken) {
        String token = accessToken.replace("Bearer ", "");
        String email = jwtTokenProvider.getEmailFromToken(token);

        refreshTokenRepository.deleteByEmail(email);
    }

}
