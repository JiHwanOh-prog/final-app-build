package com.example.finalapp.dto.board.free.file;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class FreeFileDTO {
    private Long freeFileId; // 자유게시판 파일 id
    private String originalFilename; // 파일 이름
    private String uuid; // 파일 고유 id
    private String filePath; // 파일 경로
    private String extension; // 파일 확장자
    private LocalDateTime regDate; // 파일 등록일
    private Long freeBoardId; // 자유게시판 보드 id
}
