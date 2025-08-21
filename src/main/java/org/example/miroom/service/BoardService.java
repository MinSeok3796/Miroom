package org.example.miroom.service;

import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.BoardCreateResponseDto;
import org.example.miroom.entity.Board;
import org.example.miroom.entity.BoardMember;
import org.example.miroom.entity.User;
import org.example.miroom.enums.Role;
import org.example.miroom.repository.BoardMemberRepository;
import org.example.miroom.repository.BoardRepository;
import org.example.miroom.security.AuthenticationFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final AuthenticationFacade authenticationFacade;
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final BoardListService boardListService; // ✅ 추가

    // 보드 생성
    @Transactional
    public BoardCreateResponseDto createBoard(String title, String coverImage) {
        User currentUser = authenticationFacade.getCurrentUser();

        Board board = new Board(title, coverImage);
        boardRepository.save(board);

        BoardMember boardMember = new BoardMember(board, currentUser, Role.admin);
        board.addBoardMember(boardMember);
        boardMemberRepository.save(boardMember);

        //기본 리스트 자동 생성
        String[] defaultLists = {"To-do", "In Progress", "Done"};
        Arrays.stream(defaultLists).forEach(listTitle -> boardListService.addList(board, listTitle));

        return new BoardCreateResponseDto(
                board.getBoardId(),
                board.getBoardTitle(),
                board.getCreatedAt(),
                currentUser.getId(),
                "보드가 생성되었습니다."
        );
    }

    // 멤버 추방
    @Transactional
    public String removeMember(Long boardId, Long memberId) {
        User currentUser = authenticationFacade.getCurrentUser();

        BoardMember adminMember = boardMemberRepository
                .findByBoardBoardIdAndUserId(boardId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("보드 멤버가 아닙니다."));

        if (adminMember.getRole() != Role.admin) {
            throw new SecurityException("관리자만 멤버를 내보낼 수 있습니다.");
        }

        BoardMember outMember = boardMemberRepository
                .findById(memberId).orElseThrow(() -> new RuntimeException("멤버가 존재하지 않습니다."));

        boardMemberRepository.delete(outMember);

        return outMember.getUser().getNickname() + " 멤버를 내보냈습니다.";
    }

    // 보드 삭제
    @Transactional
    public String deleteBoard(Long boardId) {
        User currentUser = authenticationFacade.getCurrentUser();
        BoardMember admin = boardMemberRepository
                .findByBoardBoardIdAndUserId(boardId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("보드 멤버가 아님."));

        if (!admin.getRole().equals(Role.admin)) {
            throw new SecurityException("관리자만 보드를 삭제할 수 있습니다.");
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("보드를 찾을 수 없음"));

        boardRepository.delete(board);

        return "보드가 삭제되었습니다.";
    }

    // 보드 조회
    @Transactional(readOnly = true)
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));
    }
}
