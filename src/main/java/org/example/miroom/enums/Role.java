package org.example.miroom.enums;

import lombok.Getter;

@Getter
public enum Role {
    admin("보드생성자"),
    manager("관리자"),
    member("보드멤버");

    public final String name;

    Role(String name) {
        this.name = name;
    }

}
