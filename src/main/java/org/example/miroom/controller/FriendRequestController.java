package org.example.miroom.controller;

import org.example.miroom.dto.FriendRequestDto;
import org.example.miroom.dto.FriendRequestResponseDto;
import org.example.miroom.enums.InvitationStatus;
import org.example.miroom.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }
    //요청하기
    @PostMapping
    public ResponseEntity<?> sendFriendRequest(@RequestBody Map<String, Long> request) {
        Long friendId = request.get("friendId");
        if (friendId == null) {
            return ResponseEntity.badRequest().body("friendId를 요청에 포함시켜야 합니다.");
        }

        try {
            friendRequestService.sendFriendRequest(friendId);
            return ResponseEntity.ok("친구 요청이 성공적으로 전송되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //요청 또는 수락
    @PatchMapping("/{requestId}")
    public ResponseEntity<?> updateFriendRequest(
            @PathVariable Long requestId,
            @RequestBody Map<String, String> body) {

        String action = body.get("action");
        friendRequestService.updateInvitationStatus(requestId, action);
        String message = "";

        if ("accept".equalsIgnoreCase(action)) {
            message = "요청을 수락하였습니다.";
        } else if ("reject".equalsIgnoreCase(action)) {
            message = "요청을 거절하였습니다.";
        } else {
            message = "알 수 없는 요청입니다.";
        }
        return ResponseEntity.ok(message);
    }

    //요청목록 조회(수락이나 거절을 하지 않은 상태)
    @GetMapping
    public ResponseEntity<List<FriendRequestResponseDto>> getFriendRequests(
            @RequestParam(value = "status", required = false) String status) {

        InvitationStatus filterStatus = null;

        if (status != null) {
            try {
                filterStatus = InvitationStatus.valueOf(status.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value : " + status);
            }
        }
        return ResponseEntity.ok(friendRequestService.getFriendRequests(filterStatus));
    }

}
