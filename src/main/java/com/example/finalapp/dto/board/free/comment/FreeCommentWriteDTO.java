package com.example.finalapp.dto.board.free.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class FreeCommentWriteDTO {
    private Long freeCommentId;
    private String content;
    private Long freeBoardId;
    private Long memberId;
}



