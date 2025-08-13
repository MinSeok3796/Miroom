package org.example.miroom.controller;

import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.BoardCreateResponseDto;
import org.example.miroom.dto.BoardCreateRequestDto;
import org.example.miroom.dto.BoardInviteDto;
import org.example.miroom.dto.BoardSummaryDto;
import org.example.miroom.service.BoardCreateService;
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

    private final BoardCreateService boardCreateService;
    private final BoardQueryService boardQueryService;
    private final BoardInviteService boardInviteService;

    //보드 생성하기
    @PostMapping
    public ResponseEntity<BoardCreateResponseDto> createBoard(
            @RequestBody BoardCreateRequestDto request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("보드 제목은 필수입니다.");
        }

        BoardCreateResponseDto response = boardCreateService.createBoard(
                request.getTitle(),
                request.getCoverImage()
        );

        return ResponseEntity.ok(response);
    }

    //보드 조회하기
    @GetMapping("/my")
    public ResponseEntity<List<BoardSummaryDto>> getMyBoards(){
        return ResponseEntity.ok(boardQueryService.getMyBoards());
    }

    //초대하기
    @PostMapping("/{boardId}/invitations")
    public ResponseEntity<Map<String,String>> inviteUser(
            @PathVariable Long boardId, @RequestBody BoardInviteDto dto) {

        String message = boardInviteService.inviteUserToBoard(boardId, dto.getUserEmail());

        return ResponseEntity.ok(Map.of("message", message));
    }
}
