package org.example.miroom.repository;

import org.example.miroom.entity.FriendRequest;
import org.example.miroom.entity.User;
import org.example.miroom.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySendUserAndReceiveUser(User sendUser, User receiveUser);
}



