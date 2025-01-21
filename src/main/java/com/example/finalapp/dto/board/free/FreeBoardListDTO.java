package com.example.finalapp.dto.board.free;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
public class FreeBoardListDTO {
    private Long freeBoardId; // 보드 게시글 id
    private String title; // 글 제목
    private int viewCount; // 조회수
    private LocalDate regDate; // 작성일
    private LocalDate modDate; // 수정일
    private Long memberId; // 작성한 회원 ID
    private String loginId; // 작성한 회원 아이디
    private int likeCount; // 좋아요 수
    private int commentCount; // 댓글 수
    
    // 파일 관련 필드 추가
    private Long freeFileId;
    private String originalFilename;
    private String uuid;
    private String filePath;
    private String extension;
}
