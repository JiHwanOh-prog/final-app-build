package com.example.finalapp.controller.board.qna;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/qna")
@RequiredArgsConstructor
public class QnaController {
    @GetMapping("/list")
    public String list() {
        return "board/qna/list";
    }

    @GetMapping("/write")
    public String write() {
        return "board/qna/write";
    }

    @GetMapping("/detail")
    public String detail() {
        return "board/qna/detail";
    }
}
