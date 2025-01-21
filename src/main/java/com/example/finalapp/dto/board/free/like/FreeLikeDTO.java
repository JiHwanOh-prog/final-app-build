package com.example.finalapp.dto.board.free.like;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class FreeLikeDTO {
    private Long freeLikeId;
    private Long freeBoardId;
    private Long memberId;
    private LocalDateTime regDate;
}




