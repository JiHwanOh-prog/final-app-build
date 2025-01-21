package com.example.finalapp.dto.board.free.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
public class FreeCommentListDTO {
    private Long freeCommentId;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Long freeBoardId;
    private Long memberId;
    private String loginId;

    public String getRegDate() {
        return regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getModDate() {
        return regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
