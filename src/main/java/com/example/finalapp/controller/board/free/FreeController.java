package com.example.finalapp.controller.board.free;

import com.example.finalapp.dto.board.PageDTO;
import com.example.finalapp.dto.board.PageRequestDTO;
import com.example.finalapp.dto.board.free.*;
import com.example.finalapp.service.board.free.FreeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board/free")
@RequiredArgsConstructor
public class FreeController {
    private final FreeBoardService freeBoardService;
    @GetMapping("/list")
    public String list(FreeBoardSearchDTO searchDTO, PageRequestDTO pageRequestDTO, Model model) {
//        List<FreeBoardListDTO> boardList = freeBoardService.getFreeBoardList();
//        List<FreeBoardListDTO> boardList = freeBoardService.getFreeBoardBySearchCondition(searchDTO);
        PageDTO<FreeBoardListDTO> pageDTO = freeBoardService.getFreeBoardsBySearchCondWithPage(searchDTO, pageRequestDTO);
//        model.addAttribute("boardList", boardList);

        model.addAttribute("pageDTO", pageDTO);
        model.addAttribute("searchDTO", searchDTO);

        return "board/free/list";
    }
    @GetMapping("/write")
    public String write(@SessionAttribute(value = "memberId", required = false) Long memberId
    , RedirectAttributes redirectAttributes ) {
        if (memberId == null) {
            redirectAttributes.addAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("message", "로그인 후 이용해주세요!");
            return "redirect:/member/login";
        }
        return "board/free/write";
    }

    @PostMapping("/write")
    public String write(FreeBoardWriteDTO boardWriteDTO,
                        @SessionAttribute("memberId") Long memberId,
                        @RequestParam(value = "image", required = false) MultipartFile imgFile) {
        // input:file로 전달하는 데이터는 MultipartFile 타입으로 받을 수 있다.
        log.debug("boardWriteDTO : {}", boardWriteDTO);
        log.debug("memberId: {}", memberId);
        // MultipartFile에 파일 데이터가 있는지 확인하려면
        // null체크가 아닌 inEmpty()로 비어있는지 확인해야함!!
        log.debug("imgFile: {}", imgFile.isEmpty());

//        freeBoardService.addFreeBoard(boardWriteDTO, memberId);

        try {
            freeBoardService.addFreeBoardWithFile(boardWriteDTO, memberId, imgFile);
        } catch (IOException e){
            e.printStackTrace();
        }

        return "redirect:/board/free/list";
    }

    @GetMapping("/detail")
    public String detail(Long freeBoardId, Model model) {

        FreeBoardDetailDTO foundBoard = freeBoardService.getFreeBoardById(freeBoardId);
        model.addAttribute("board", foundBoard);

        return "board/free/detail";
    }
    @GetMapping("/modify")
    public String modify(Long freeBoardId, Model model) {
        FreeBoardDetailDTO foundBoard = freeBoardService.getFreeBoardById(freeBoardId);
        model.addAttribute("board", foundBoard);

        return "board/free/modify";
    }

    @PostMapping("/modify")
    public String modify(FreeBoardModifyDTO boardModifyDTO,
                         @RequestParam(value = "image", required = false) MultipartFile imgFile,
                         RedirectAttributes redirectAttributes) {
        try {
            freeBoardService.modifyFreeBoardWithFile(boardModifyDTO, imgFile);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        redirectAttributes.addAttribute("freeBoardId", boardModifyDTO.getFreeBoardId());

        return "redirect:/board/free/detail";
    }

    @GetMapping("/delete")
    public String delete(Long freeBoardId){
        freeBoardService.removeFreeBoard(freeBoardId);
        return "redirect:/board/free/list";
    }
}
