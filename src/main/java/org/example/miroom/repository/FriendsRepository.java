package org.example.miroom.repository;

import org.example.miroom.entity.Friend;
import org.example.miroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendsRepository extends JpaRepository<Friend, Long> {

    // 친구목록
    List<Friend> findByFromUser(User fromUser);

    // 이미 친구인지
    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    // 친구삭제
    void deleteByFromUserAndToUser(User fromUser, User toUser);
}
