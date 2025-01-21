package com.example.finalapp.dto.board.free;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class FreeBoardModifyDTO {
    private Long freeBoardId;
    private String title;
    private String content;
}
