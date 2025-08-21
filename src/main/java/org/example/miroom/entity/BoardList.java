package org.example.miroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "list")
public class BoardList extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Setter
    @Column(name = "list_title", length = 20, nullable = false)
    private String listTitle;

    @Setter
    @Column(nullable = false)
    private Long position;

    public BoardList(){}

    public BoardList(Board board, String listTitle, Long position){
        this.board = board;
        this.listTitle = listTitle;
        this.position = position;
    }


}
