package org.example.miroom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.FriendDto;
import org.example.miroom.entity.Friend;
import org.example.miroom.entity.User;
import org.example.miroom.repository.FriendRequestRepository;
import org.example.miroom.repository.FriendsRepository;
import org.example.miroom.repository.UserRepository;
import org.example.miroom.security.AuthenticationFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final AuthenticationFacade authenticationFacade;
    private final FriendsRepository friendRepository;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    //친구 목록 조회
    public List<FriendDto> getFriendList() {
        User currentUser = authenticationFacade.getCurrentUser();

        List<Friend> friendEntities = friendRepository.findByFromUser(currentUser);

        return friendEntities.stream()
                .map(friend -> new FriendDto(
                        friend.getFriendId(),
                        friend.isFavorite(),
                        friend.getToUser().getNickname(),
                        friend.getToUser().getEmail()
                ))
                .collect(Collectors.toList());
    }
    //즐겨찾기 및 해제
    @Transactional
    public void updateFavorite(Long friendId, boolean favorite) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("친구가 없습니다."));
        friend.setFavorite(favorite);
    }

    @Transactional
    public String deleteFriend(Long toUserId) {
        // 현재 로그인한 사용자
        String currentUser = authenticationFacade.getCurrentUser().getEmail();

        User fromUser = userRepository.findByEmail(currentUser)
                .orElseThrow(() -> new RuntimeException("로그인한 유저를 찾을 수 없습니다."));

        // 삭제할 대상 유저
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("삭제할 유저를 찾을 수 없습니다."));

        // 친구 여부 확인
        if (!friendRepository.existsByFromUserAndToUser(fromUser, toUser)) {
            throw new RuntimeException("친구 관계가 아닙니다.");
        }

        // 삭제
        friendRepository.deleteByFromUserAndToUser(fromUser, toUser);
        friendRepository.deleteByFromUserAndToUser(toUser, fromUser); // 양방향 삭제

        //친구 요청목록도 삭제하기
        friendRequestRepository.deleteBySendUserAndReceiveUser(fromUser, toUser);
        friendRequestRepository.deleteBySendUserAndReceiveUser(toUser, fromUser);
        return "친구가 성공적으로 삭제되었습니다.";
    }
}
