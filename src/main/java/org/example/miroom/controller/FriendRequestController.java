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
    public ResponseEntity<Map<String, String>> sendRequest(@RequestBody FriendRequestDto dto) {
        String message = friendRequestService.sendFriendRequest(dto.getFriendId());
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PatchMapping("/{requestId}")
    public ResponseEntity<Map<String, String>> updateRequest(
            @PathVariable Long requestId,
            @RequestParam String action) {
        String message = friendRequestService.updateInvitationStatus(requestId, action);
        return ResponseEntity.ok(Map.of("message", message));
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
                throw new IllegalArgumentException("잘못된 상태 값입니다. : " + status);
            }
        }
        return ResponseEntity.ok(friendRequestService.getFriendRequests(filterStatus));
    }

}
