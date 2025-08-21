package org.example.miroom.repository;

import org.example.miroom.entity.Board;
import org.example.miroom.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardListRepository extends JpaRepository<BoardList, Long> {
    List<BoardList> findByBoardOrderByPosition(Board board);
}
