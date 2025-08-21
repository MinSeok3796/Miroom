package org.example.miroom.service;

import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.BoardSummaryDto;
import org.example.miroom.entity.BoardMember;
import org.example.miroom.entity.User;
import org.example.miroom.repository.BoardMemberRepository;
import org.example.miroom.security.AuthenticationFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardQueryService {
    private final BoardMemberRepository boardMemberRepository;
    private final AuthenticationFacade authenticationFacade;

    //보드 조회
    public List<BoardSummaryDto> getMyBoards() {
        User currentUser = authenticationFacade.getCurrentUser();

        List<BoardMember> memberships = boardMemberRepository.findByUser(currentUser);

        return memberships.stream()
                .map(BoardMember::getBoard)
                .map(board -> new BoardSummaryDto(
                        board.getBoardId(),
                        board.getBoardTitle(),
                        board.getCoverImage()
                ))
                .collect(Collectors.toList());
    }
}
