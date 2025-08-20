package org.example.miroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.miroom.entity.FriendRequest;
import org.example.miroom.enums.InvitationStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FriendRequestResponseDto {
    private String senderNickname;
    private InvitationStatus status;
    private LocalDateTime sentAt;

    public static FriendRequestResponseDto fromEntity(FriendRequest friendRequest) {
        return new FriendRequestResponseDto(
                friendRequest.getSendUser().getNickname(),
                friendRequest.getInvitationStatus(),
                friendRequest.getCreatedAt()
        );
    }
}