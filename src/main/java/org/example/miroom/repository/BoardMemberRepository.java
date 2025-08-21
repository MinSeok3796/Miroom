package org.example.miroom.repository;

import org.example.miroom.entity.Board;
import org.example.miroom.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.miroom.entity.User;

import java.util.List;
import java.util.Optional;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
    List<BoardMember> findByUser(User user);

    Optional<BoardMember> findByBoardAndUser(Board board, User user);

    boolean existsByBoardAndUser(Board board, User user);
    Optional<BoardMember> findByBoardBoardIdAndUserId(Long boardId, Long userId);
//    Optional<BoardMember> findByBoardIdAndId(Long boardId, Long memberId);
}
