package com.example.finalapp.dto.board.free.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class FreeCommentModifyDTO {
    Long freeCommentId;
    String content;
}
