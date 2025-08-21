package org.example.miroom.service;

import lombok.RequiredArgsConstructor;
import org.example.miroom.entity.Board;
import org.example.miroom.entity.BoardMember;
import org.example.miroom.entity.BoardRequest;
import org.example.miroom.entity.User;
import org.example.miroom.enums.InvitationStatus;
import org.example.miroom.enums.Role;
import org.example.miroom.repository.BoardMemberRepository;
import org.example.miroom.repository.BoardRepository;
import org.example.miroom.repository.BoardRequestRepository;
import org.example.miroom.repository.UserRepository;
import org.example.miroom.security.AuthenticationFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardInviteService {

    private final BoardRequestRepository boardRequestRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final BoardMemberRepository boardMemberRepository;

    //초대하기
    @Transactional
    public String inviteUserToBoard(Long boardId, String inviteeEmail) {
        User inviter = authenticationFacade.getCurrentUser();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보드입니다."));

        // 관리자 여부 체크
        BoardMember inviterMember = boardMemberRepository.findByBoardAndUser(board, inviter)
                .orElseThrow(() -> new IllegalStateException("보드 멤버가 아닙니다."));

        if (!inviterMember.getRole().equals(Role.admin)) {
            throw new IllegalStateException("관리자만 초대할 수 있습니다.");
        }

        //없어
        User invitee = userRepository.findByEmail(inviteeEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //이미초대
        boolean existsPending = boardRequestRepository.existsByBoardAndInviteeAndStatus(board, invitee, InvitationStatus.PENDING);
        if (existsPending) {
            throw new IllegalStateException("이미 초대 요청이 대기 중입니다.");
        }

        BoardRequest request = new BoardRequest(inviter, invitee, board);
        boardRequestRepository.save(request);

        return "초대 요청이 전송되었습니다.";
    }

    // 초대요청처리
    @Transactional
    public String respondToBoardInvitation(Long boardRequestId, String action) {
        BoardRequest request = boardRequestRepository.findById(boardRequestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 초대 요청입니다."));

        if (request.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 초대 요청입니다.");
        }

        if (action.equalsIgnoreCase("accept")) {
            request.accept();

            Board board = request.getBoard();
            User invitee = request.getInvitee();

            boolean alreadyMember = boardMemberRepository.existsByBoardAndUser(board, invitee);
            if (!alreadyMember) {
                BoardMember newMember = new BoardMember(board, invitee, Role.member);
                board.addBoardMember(newMember);
                boardMemberRepository.save(newMember);
            }

            return "초대 요청이 수락되었습니다.";

        } else if (action.equalsIgnoreCase("reject")) {
            request.reject();
            return "초대 요청이 거절되었습니다.";
        } else {
            throw new IllegalArgumentException("잘못된 액션입니다. accept 또는 reject만 가능합니다.");
        }
    }


}
