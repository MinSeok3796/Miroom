package org.example.miroom.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키값 자동생성
    private Long id;

    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE", name = "alarm_enabled")
    private boolean alarmEnabled;

    private String email;

    public User(){}

    public User(String nickname, String phoneNumber, String password, boolean alarmEnabled, String email) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.alarmEnabled = alarmEnabled;
        this.email = email;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> boardMembers = new ArrayList<>();

    public void addBoardMember(BoardMember boardMember) {
        boardMembers.add(boardMember);
        boardMember.setUser(this);
    }


}
