package org.example.miroom.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.miroom.enums.InvitationStatus;

@Getter
@Entity
@Table(name = "friend_request",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"send_user_id", "receive_user_id"})
})
public class FriendRequest extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id", nullable = false)
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id", nullable = false)
    private User receiveUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "invitation_status", nullable = false)
    private InvitationStatus invitationStatus;

    public FriendRequest(){}

    public FriendRequest(User sendUser, User receiveUser) {
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
        this.invitationStatus = InvitationStatus.PENDING;
    }

    public void accept(){
        this.invitationStatus = InvitationStatus.ACCEPTED;
    }

    public void reject(){
        this.invitationStatus = InvitationStatus.REJECTED;
    }


}
