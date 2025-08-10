package org.example.miroom.controller;

import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.FriendDto;
import org.example.miroom.entity.Friend;
import org.example.miroom.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendsController {
    private final FriendService friendService;

    //친구조회
    @GetMapping
    public ResponseEntity<List<FriendDto>> getFriends() {
        List<FriendDto> friends = friendService.getFriendList();
        return ResponseEntity.ok(friends);
    }
}
