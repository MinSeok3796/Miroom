package org.example.miroom.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "reaction", uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "user_id", "emoji"}))
public class Reaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "emoji", nullable = false, length = 50)
    private String emoji;

    public Reaction(){}

    public Reaction(Comment comment, User user, String emoji) {
        this.comment = comment;
        this.user = user;
        this.emoji = emoji;
    }

}
