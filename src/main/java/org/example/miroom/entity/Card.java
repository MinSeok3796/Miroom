package org.example.miroom.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "card")
public class Card extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private BoardList list;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Label> labels = new ArrayList<>();

    @Column(name = "card_title",length = 20, nullable = false)
    private String cardTitle;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "position")
    private Long position;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    public Card(){}

    public Card(BoardList list, String cardTitle, Long position){
        this.list = list;
        this.cardTitle = cardTitle;
        this.position = position;
        this.isCompleted = false;
    }

    public void setStartDate(LocalDateTime startDate){
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate){
        this.endDate = endDate;
    }


}
