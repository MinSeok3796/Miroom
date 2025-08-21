package org.example.miroom.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.ListCreateRequestDto;
import org.example.miroom.dto.ListCreateResponseDto;
import org.example.miroom.dto.UpdateListTitleRequest;
import org.example.miroom.entity.Board;
import org.example.miroom.entity.BoardList;
import org.example.miroom.service.BoardListService;
import org.example.miroom.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardListController {

    private final BoardService boardService;
    private final BoardListService boardListService;

    //리스트조회
    @GetMapping("/{boardId}/lists")
    public ResponseEntity<List<ListCreateResponseDto>> getLists(@PathVariable Long boardId) {
        Board board = boardService.getBoardById(boardId);
        List<ListCreateResponseDto> lists = boardListService.getListsByBoard(board)
                .stream()
                .map(list -> new ListCreateResponseDto(
                        list.getListId(),
                        board.getBoardId(),
                        list.getListTitle(),
                        list.getPosition()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(lists);
    }

    // 새 리스트 추가
    @PostMapping("/{boardId}/lists")
    public ResponseEntity<?> addList(
            @PathVariable Long boardId,
            @RequestBody ListCreateRequestDto dto) {

        Board board = boardService.getBoardById(boardId);

        //타이틀 빈칸시
        if (dto.getListTitle() == null || dto.getListTitle().isBlank()) {
            return ResponseEntity.badRequest().body(new MessageResponse("리스트 제목은 필수."));
        }
        //중복
        boolean exists = boardListService.getListsByBoard(board)
                .stream()
                .anyMatch(list -> list.getListTitle().equalsIgnoreCase(dto.getListTitle()));

        if (exists) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("이미 존재하는 리스트 제목입니다."));
        }

        BoardList newList = boardListService.addList(board, dto.getListTitle());

        ListCreateResponseDto response = new ListCreateResponseDto(
                newList.getListId(),
                board.getBoardId(),
                newList.getListTitle(),
                newList.getPosition()
        );


        return ResponseEntity.ok(response);
    }

    //리스트 업뎃
    @PatchMapping("/{boardId}/lists/{listId}")
    public ResponseEntity<?> updateList(@PathVariable Long boardId, @PathVariable Long listId, @RequestBody UpdateListTitleRequest dto){
        Board board = boardService.getBoardById(boardId);

        if (dto.getNewTitle() == null || dto.getNewTitle().isBlank()) {
            return ResponseEntity.badRequest().body(new MessageResponse("리스트 제목은 필수에요!"));
        }

        boolean exists = boardListService.getListsByBoard(board).stream().
                anyMatch(list -> !list.getListId().equals(listId) && list.getListTitle().equalsIgnoreCase(dto.getNewTitle()));

        if  (exists) {
            return ResponseEntity.badRequest().body(new MessageResponse("이미 존재하는 리스트 이름이에요!"));
        }

        BoardList updateList = boardListService.updateList(listId, dto.getNewTitle());

        ListCreateResponseDto response = new ListCreateResponseDto(
                updateList.getListId(),
                board.getBoardId(),
                updateList.getListTitle(),
                updateList.getPosition()
        );
        return ResponseEntity.ok(response);
    }

    //삭제!
    @DeleteMapping("/{boardId}/lists/{listId}")
    public ResponseEntity<?> deleteList(@PathVariable Long boardId, @PathVariable Long listId){
        boardService.getBoardById(boardId);
        boardListService.deleteList(listId);

        return ResponseEntity.ok(new MessageResponse("리스트 삭제했어요."));
    }

    //오류메시지
    @Getter
    public static class MessageResponse {
        private final String message;
        public MessageResponse(String message) { this.message = message; }
    }
}
