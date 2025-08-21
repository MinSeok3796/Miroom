package org.example.miroom.service;

import jakarta.transaction.Transactional;
import org.example.miroom.dto.SignupRequest;
import org.example.miroom.entity.User;
import org.example.miroom.repository.FriendRequestRepository;
import org.example.miroom.repository.FriendsRepository;
import org.example.miroom.repository.RefreshTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.miroom.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FriendsRepository friendsRepository;
    private final FriendRequestRepository friendRequestRepository;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository, FriendsRepository friendsRepository, FriendRequestRepository friendRequestRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.friendsRepository = friendsRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    //회원가입
    @Transactional
    public User signup(SignupRequest request) {
        //중복 전화번호
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
        }
        //중복 이메일
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //유저 객체
        User user = new User(
                request.getNickname(),
                request.getPhoneNumber(),
                encodedPassword,
                request.isAlarmEnabled(),
                request.getEmail()
        );
        //저장
        userRepository.save(user);
        return user;
    }

    //탈퇴
    @Transactional
    public String withdraw(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 회원입니다."));
        //친구도 삭제하기
        friendsRepository.deleteByFromUserOrToUser(user, user);
        //요청목록도 삭제하기
        friendRequestRepository.deleteBySendUserOrReceiveUser(user, user);
        // 토큰 삭제
        refreshTokenRepository.deleteByEmail(email);
        // 회원 삭제
        userRepository.delete(user);

        return "탈퇴가 완료되었습니다.";
    }
}
