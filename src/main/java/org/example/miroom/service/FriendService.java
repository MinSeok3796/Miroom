package org.example.miroom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.miroom.dto.FriendDto;
import org.example.miroom.entity.Friend;
import org.example.miroom.entity.User;
import org.example.miroom.repository.FriendsRepository;
import org.example.miroom.security.AuthenticationFacade;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final AuthenticationFacade authenticationFacade;
    private final FriendsRepository friendRepository;

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
}
