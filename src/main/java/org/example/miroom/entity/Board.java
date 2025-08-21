package org.example.miroom.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "board")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "board_title", nullable = false, length = 10)
    private String boardTitle;

    @Column(name = "cover_image", length = 512)
    private String coverImage;

    public Board() {}

    public Board(String boardTitle, String coverImage) {
        this.boardTitle = boardTitle;
        this.coverImage = coverImage;
    }

    // 보드 멤버
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> boardMembers = new ArrayList<>();

    // 보드 초대(Invitation)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardRequest> boardRequests = new ArrayList<>();

    public void addBoardMember(BoardMember boardMember) {
        boardMembers.add(boardMember);
        boardMember.setBoard(this);
    }

}
