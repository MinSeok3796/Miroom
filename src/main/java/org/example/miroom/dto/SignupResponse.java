package org.example.miroom.dto;

import lombok.Getter;
import org.example.miroom.entity.User;

public class SignupResponse {
    @Getter
    private String email;
    @Getter
    private String nickname;
    @Getter
    private String phoneNumber;
    @Getter
    private String message;

    public SignupResponse(User user) {
        this.email= user.getEmail();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.message = "회원가입이 완료되었습니다.";
    }
}
