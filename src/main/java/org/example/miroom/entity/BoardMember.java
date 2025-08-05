package org.example.miroom.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.example.miroom.enums.Role;

@Getter
@Entity
@Table(name = "board_member")
public class BoardMember extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public BoardMember() {}

    public BoardMember(Board board, User user, Role role) {
        this.board = board;
        this.user = user;
        this.role = role;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
