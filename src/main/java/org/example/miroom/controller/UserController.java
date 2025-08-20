package org.example.miroom.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.miroom.entity.User;
import org.example.miroom.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.miroom.dto.SignupRequest;
import org.example.miroom.dto.SignupResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor  // Lombok 생성자 자동 생성
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest request) {
        User user = userService.signup(request);
        SignupResponse response = new SignupResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}