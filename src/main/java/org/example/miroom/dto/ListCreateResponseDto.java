package org.example.miroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListCreateResponseDto {
    private Long listId;
    private Long boardId;
    private String listTitle;
    private Long position;
}
