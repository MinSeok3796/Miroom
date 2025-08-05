package org.example.miroom.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="friend")
public class Friend extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @Column(name = "is_favorite")
    private boolean isFavorite;

    public Friend(){}

    public Friend(User fromUser, User toUser, Boolean isFavorite){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.isFavorite = isFavorite;
    }
}
