package org.example.miroom.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.miroom.entity.Board;
import org.example.miroom.entity.BoardList;
import org.example.miroom.repository.BoardListRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardListService {

    private final BoardListRepository boardListRepository;


    //리스트 추가
    @Transactional
    public BoardList addList(Board board, String listTitle) {
        List<BoardList> lists = boardListRepository.findByBoardOrderByPosition(board);
        Long maxPosition = lists.stream()
                .map(BoardList::getPosition)
                .max(Long::compare)
                .orElse(-1L);

        BoardList newList = new BoardList(board, listTitle, maxPosition + 1);
        return boardListRepository.save(newList);
    }

    //조회용
    @Transactional(readOnly = true)
    public List<BoardList> getListsByBoard(Board board) {
        return boardListRepository.findByBoardOrderByPosition(board);
    }

    //업뎃
    @Transactional
    public BoardList updateList(Long listId, String listTitle) {
        BoardList list = boardListRepository.findById(listId).orElseThrow(()-> new RuntimeException("Not Found List"));
        list.setListTitle(listTitle);
        return boardListRepository.save(list);
    }

    //삭제
    @Transactional
    public void deleteList(Long listId) {
        BoardList list = boardListRepository.findById(listId).orElseThrow(()-> new RuntimeException("Not Found List"));
        boardListRepository.delete(list);
    }
}
