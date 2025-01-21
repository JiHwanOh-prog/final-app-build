package com.example.finalapp.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 페이지 요청을 받기위한 DTO
@Getter @Setter @ToString
public class PageRequestDTO {
    private int page;
    private int size;

    // 아무런 값 없이 해당 DTO만 전달되면 기본 생성자 실행됨!
    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }
}
