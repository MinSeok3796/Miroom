package org.example.miroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardCreateResponseDto {
    private Long boardId;
    private String boardTitle;
    private LocalDateTime createdAt;
    private Long userId;
    private String message;
}
