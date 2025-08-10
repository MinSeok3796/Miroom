package org.example.miroom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendDto {

    @JsonProperty("friend_id")
    private Long friendId;

    @JsonProperty("is_favorite")
    private boolean favorite;  // 필드명을 isFavorite → favorite 으로 변경

    private String nickname;
    private String email;
}
