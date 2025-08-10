package org.example.miroom.service;

import jakarta.transaction.Transactional;
import org.example.miroom.dto.SignupRequest;
import org.example.miroom.entity.User;
import org.example.miroom.repository.RefreshTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.miroom.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    @Transactional
    public User signup(SignupRequest request) {
        //중복 전화번호
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
        }
        //중복 이메일
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //유저 객체
        User user = new User(
                request.getNickname(),
                request.getPhoneNumber(),
                encodedPassword,
                request.isAlarmEnabled(),
                request.getEmail()
        );
        //저장
        userRepository.save(user);
        return user;
    }
    @Transactional
    public void withdraw(String email) {
        // 토큰 삭제
        refreshTokenRepository.deleteByEmail(email);
        // 회원 삭제
        userRepository.deleteByEmail(email);
    }
}
