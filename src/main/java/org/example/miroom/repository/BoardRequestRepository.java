package org.example.miroom.repository;

import org.example.miroom.entity.Board;
import org.example.miroom.entity.BoardRequest;
import org.example.miroom.entity.User;
import org.example.miroom.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRequestRepository extends JpaRepository<BoardRequest, Long> {
    boolean existsByBoardAndInviteeAndStatus(Board board, User invitee, InvitationStatus status);
}
