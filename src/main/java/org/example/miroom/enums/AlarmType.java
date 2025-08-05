package org.example.miroom.enums;

import lombok.Getter;

@Getter
public enum AlarmType {
    COMMENT("댓글알림"),
    LIST("리스트알림"),
    FRIEND("친구알림"),
    BOARD("보드알림"),
    CARD("카드알림");

    private final String name;

    private AlarmType(String name) {
        this.name = name;
    }

}
