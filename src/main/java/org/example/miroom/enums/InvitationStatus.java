package org.example.miroom.enums;

import lombok.Getter;

@Getter
public enum InvitationStatus {
    PENDING("대기중"),
    ACCEPTED("수락됨"),
    REJECTED("거절됨");

    private final String label;

    InvitationStatus(String label) {
        this.label = label;
    }

}
