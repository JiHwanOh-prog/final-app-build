package com.example.finalapp.dto.board.free;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter @ToString
public class FreeBoardDetailDTO {
    private Long freeBoardId;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private Long memberId;
    private String loginId;
    private int likeCount;

    // 나중에 추가
    // 이미지 파일 정보
    private Long freeFileId;
    private String originalFilename;
    private String uuid;
    private String filePath;
    private String extension;

    // 날짜 타입 디테일 페이지에 맞게 포매팅하기
    public String getRegDate() {
        return regDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getModDate() {
        return modDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
