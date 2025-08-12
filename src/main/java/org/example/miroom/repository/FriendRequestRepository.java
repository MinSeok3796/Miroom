package org.example.miroom.repository;

import org.example.miroom.entity.FriendRequest;
import org.example.miroom.entity.User;
import org.example.miroom.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    boolean existsBySendUserAndReceiveUser(User sendUser, User receiveUser);
    List<FriendRequest> findByReceiveUser(User receiveUser);
    List<FriendRequest> findByReceiveUserAndInvitationStatus(User receiveUser, InvitationStatus invitationStatus);

    //요청도 삭제(이미 처리된거) 안그러면 다시 친구 못함
    void deleteBySendUserAndReceiveUser(User sendUser, User receiveUser);

    @Transactional
    void deleteBySendUserOrReceiveUser(User sendUser, User receiveUser);
}



