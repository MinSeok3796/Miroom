package org.example.miroom.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.example.miroom.enums.InvitationStatus;

@Entity
@Getter
@Setter
@Table(name = "board_request")
public class BoardRequest extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    private User inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    private User invitee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Enumerated(EnumType.STRING)
    @Column(name = "invitation_status",nullable = false)
    private InvitationStatus status = InvitationStatus.PENDING;

    public BoardRequest(){}

    public BoardRequest(User inviter, User invitee, Board board){
        this.inviter = inviter;
        this.invitee = invitee;
        this.board = board;
    }

    public void accept(){
        this.status = InvitationStatus.ACCEPTED;
    }

    public void reject(){
        this.status = InvitationStatus.REJECTED;
    }

}
