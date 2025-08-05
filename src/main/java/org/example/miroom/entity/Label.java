package org.example.miroom.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "label")
public class Label extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "label_title", nullable = false)
    private String labelTitle;

    @Column(name = "label_color", nullable = false)
    private String labelColor;

    public Label(){}

    public Label(Card card, String labelTitle, String labelColor){
        this.card = card;
        this.labelTitle = labelTitle;
        this.labelColor = labelColor;
    }

}
