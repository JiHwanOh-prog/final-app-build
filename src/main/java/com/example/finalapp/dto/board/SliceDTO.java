package com.example.finalapp.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class SliceDTO<T> {
    private int page;   // 현재 페이지 번호
    private int size;   // 한 페이지 당 게시글 수
    private boolean hasNext;
    private List<T> list; // 실질적인 데이터 (한 페이지에 해당되는 게시물 리스트)

    public SliceDTO(int page, int size, List<T> list) {
        this.page = page;
        this.size = size;
        this.list = list;
        // 다음 페이지 존재 여부를 반환하는 필드
        this.hasNext = list.size() > this.size;
        // 무한 스크롤 페이징을 사용하는 목적은 total을 구하지 않기 위함
        // total 조회 쿼리는 성능 저하의 주요 원인이므로 total 없이 페이징이 가능하다면
        // 성능 측면에서 유리함!

        // Slice 방식의 원리
        // 한 페이지 당 10개의 댓글이 필요하다면 항상 11개를 조회함
        // 만약 11개가 조회되었다면, 다음 페이지가 존재한다는 의미
        // 실제 데이터는 10개만 화면에 보여줘야하므로 마지막 (11번째) 댓글은 삭제
        if(this.hasNext) {
            list.removeLast();
        }
    }
}
