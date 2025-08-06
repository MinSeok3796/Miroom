package org.example.miroom.service;

import org.example.miroom.dto.SignupRequest;
import org.example.miroom.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.miroom.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(SignupRequest request) {

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getNickname(),
                request.getPhoneNumber(),
                encodedPassword,
                request.isAlarmEnabled(),
                request.getEmail()
        );

        userRepository.save(user);
        return user;
    }

}
