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

@Service
@RequiredArgsConstructor
public class BoardCreateService {

    private final AuthenticationFacade authenticationFacade;
    private final BoardRepository boardRepository;
    private final BoardMemberRepository boardMemberRepository;

    //생성하면
    @Transactional
    public BoardCreateResponseDto createBoard(String title, String coverImage) {
        User currentUser = authenticationFacade.getCurrentUser();

        Board board = new Board(title, coverImage);
        boardRepository.save(board);

        BoardMember boardMember = new BoardMember(board, currentUser, Role.admin);
        board.addBoardMember(boardMember);
        boardMemberRepository.save(boardMember);

        return new BoardCreateResponseDto(
                board.getBoardId(),
                board.getBoardTitle(),
                board.getCreatedAt(),
                currentUser.getId(),
                "보드가 생성되었습니다."
        );
    }
}
