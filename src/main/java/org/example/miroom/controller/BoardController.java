package org.example.miroom.controller;

import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.BoardCreateRequestDto;
import org.example.miroom.dto.BoardCreateResponseDto;
import org.example.miroom.dto.BoardInviteDto;
import org.example.miroom.dto.BoardSummaryDto;
import org.example.miroom.service.BoardListService;
import org.example.miroom.service.BoardService;
import org.example.miroom.service.BoardInviteService;
import org.example.miroom.service.BoardQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardQueryService boardQueryService;
    private final BoardInviteService boardInviteService;

    // 보드 생성
    @PostMapping
    public ResponseEntity<BoardCreateResponseDto> createBoard(
            @RequestBody BoardCreateRequestDto request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("보드 제목은 필수입니다.");
        }

        BoardCreateResponseDto response = boardService.createBoard(
                request.getTitle(),
                request.getCoverImage()
        );

        return ResponseEntity.ok(response);
    }

    //보드 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<BoardSummaryDto>> getMyBoards(){
        return ResponseEntity.ok(boardQueryService.getMyBoards());
    }

    //보드 초대
    @PostMapping("/{boardId}/invitations")
    public ResponseEntity<Map<String,String>> inviteUser(
            @PathVariable Long boardId, @RequestBody BoardInviteDto dto) {

        String message = boardInviteService.inviteUserToBoard(boardId, dto.getEmail());
        return ResponseEntity.ok(Map.of("message", message));
    }

    // 초대요청 처리
    @PatchMapping("/invitations/{boardRequestId}")
    public ResponseEntity<Map<String, String>> respondToInvitation(
            @PathVariable Long boardRequestId,
            @RequestBody Map<String, String> body) {

        String action = body.get("action");
        String message = boardInviteService.respondToBoardInvitation(boardRequestId, action);
        return ResponseEntity.ok(Map.of("message", message));
    }

    // 멤버 추방
    @DeleteMapping("/{boardId}/Members/{userId}")
    public ResponseEntity<Map<String, String>> removeMember(
            @PathVariable Long boardId,
            @PathVariable Long userId) {

        String message = boardService.removeMember(boardId, userId);
        return ResponseEntity.ok(Map.of("message", message));
    }

    // 보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, String>> deleteBoard(@PathVariable Long boardId) {
        String message = boardService.deleteBoard(boardId);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
