package org.example.miroom.dto;
import org.example.miroom.entity.User;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String email;
    private final String accessToken;
    private final String refreshToken;

    public LoginResponse(User user, String accessToken, String refreshToken) {
        this.email = user.getEmail();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
