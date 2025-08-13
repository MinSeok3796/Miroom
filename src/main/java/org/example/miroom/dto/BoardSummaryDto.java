package org.example.miroom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class BoardSummaryDto {
    private Long BoardId;
    private String Title;
    private String coverImage;

}
