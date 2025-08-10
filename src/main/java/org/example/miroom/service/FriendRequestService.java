package org.example.miroom.service;

import jakarta.transaction.Transactional;
import org.example.miroom.entity.Friend;
import org.example.miroom.entity.FriendRequest;
import org.example.miroom.entity.User;
import org.example.miroom.enums.InvitationStatus;
import org.example.miroom.repository.FriendsRepository;
import org.example.miroom.repository.FriendRequestRepository;
import org.example.miroom.repository.UserRepository;
import org.example.miroom.security.AuthenticationFacade;
import org.springframework.stereotype.Service;

@Service
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendsRepository friendsRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    public FriendRequestService(FriendRequestRepository friendRequestRepository,
                                FriendsRepository friendsRepository,
                                UserRepository userRepository,
                                AuthenticationFacade authenticationFacade) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendsRepository = friendsRepository;
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
    }

    // 요청하기
    @Transactional
    public void sendFriendRequest(Long friendId) {
        Long currentUserId = authenticationFacade.getCurrentUserId();
        User sendUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보가 없습니다."));

        if (sendUser.getId().equals(friendId)) {
            throw new IllegalArgumentException("자기 자신에게 친구 요청할 수 없습니다.");
        }

        User receiveUser = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        boolean exists = friendRequestRepository.existsBySendUserAndReceiveUser(sendUser, receiveUser);
        if (exists) {
            throw new IllegalStateException("이미 친구 요청을 보냈습니다.");
        }

        FriendRequest friendRequest = new FriendRequest(sendUser, receiveUser);
        friendRequestRepository.save(friendRequest);
    }

    // 요청 수락/거절 처리
    @Transactional
    public void updateInvitationStatus(Long requestId, String action) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 친구 요청입니다."));

        if (request.getInvitationStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 친구 요청입니다.");
        }

        if (action.equalsIgnoreCase("accept")) {
            request.accept();

            User sender = request.getSendUser();
            User receiver = request.getReceiveUser();

            // 친구 테이블에 양방향 저장 (중복 확인 후)
            if (!friendsRepository.existsByFromUserAndToUser(sender, receiver)) {
                friendsRepository.save(new Friend(sender, receiver, false));
            }
            if (!friendsRepository.existsByFromUserAndToUser(receiver, sender)) {
                friendsRepository.save(new Friend(receiver, sender, false));
            }

        } else if (action.equalsIgnoreCase("reject")) {
            request.reject();
        }
    }
}
