package org.example.miroom.controller;

import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.FriendDto;
import org.example.miroom.security.AuthenticationFacade;
import org.example.miroom.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendsController {
    private final FriendService friendService;
    private final AuthenticationFacade auth;

    //친구조회
    @GetMapping
    public ResponseEntity<List<FriendDto>> getFriends() {
        List<FriendDto> friends = friendService.getFriendList();
        return ResponseEntity.ok(friends);
    }

    //즐찾
    @PatchMapping("/{friendId}/favorite")
    public ResponseEntity<?> updateFavorite(
            @PathVariable Long friendId,
            @RequestBody Map<String, Boolean> request) {

        Boolean favorite = request.get("favorite");
        if (favorite == null) {
            return ResponseEntity.badRequest().body("favorite 값을 보내야 합니다.");
        }

        friendService.updateFavorite(friendId, favorite);
        String message = favorite ? "즐겨찾기 설정 완료" : "즐겨찾기 해제 완료";
        return ResponseEntity.ok(message);
    }

    //친구 삭제
    @DeleteMapping("/{toUserId}")
    public ResponseEntity<Map<String, String>> deleteFriend(@PathVariable Long toUserId) {
        String message = friendService.deleteFriend(toUserId);
        Map<String, String> response = Map.of("message",message);
        return ResponseEntity.ok(response);
    }
}
