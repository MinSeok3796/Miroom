package org.example.miroom.security;

import org.example.miroom.entity.User;
import org.example.miroom.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserRepository userRepository;

    public AuthenticationFacadeImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증 정보가 없습니다.");
        }

        String email = authentication.getName(); // 일반적으로 username 혹은 email

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("로그인한 사용자를 찾을 수 없습니다."));
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
