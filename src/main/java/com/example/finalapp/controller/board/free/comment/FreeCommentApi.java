package com.example.finalapp.controller.board.free.comment;

import com.example.finalapp.dto.board.PageRequestDTO;
import com.example.finalapp.dto.board.SliceDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentListDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentModifyDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentWriteDTO;
import com.example.finalapp.service.board.free.comment.FreeCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FreeCommentApi {
    private final FreeCommentService freeCommentService;

    // add 댓글 내용 입력
    @PostMapping("/v1/free-boards/{freeBoardId}/comments")
    public void postFreeComment(@PathVariable("freeBoardId") Long freeBoardId,
                                @RequestBody FreeCommentWriteDTO commentWriteDTO, // content는 DTO 필드에 있으므로 DTO 필드를 Api를 통해 넘겨줌으로써 데이터를 활용할 수 있다.
                                @SessionAttribute("memberId") Long memberId) {
        freeCommentService.addFreeComment(commentWriteDTO, freeBoardId, memberId); // service에서 사용하는 메서드를 api로 만들어줌
    }

    // 댓글 목록 조회
    @GetMapping("/v1/free-boards/{freeBoardId}/comments")
    public Map<String, Object> getFreeComments(@PathVariable("freeBoardId") Long freeBoardId,
                                               PageRequestDTO pageRequestDTO) {
        int total = freeCommentService.getTotalByBoardId(freeBoardId);
//        List<FreeCommentListDTO> commentList = freeCommentService.getFreeComments(freeBoardId);
        SliceDTO<FreeCommentListDTO> sliceDTO = freeCommentService.getFreeCommentsByBoardIdWithSlice(freeBoardId, pageRequestDTO);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
//        resultMap.put("commentList", commentList);
        resultMap.put("sliceDTO", sliceDTO);
        return resultMap;
    }

    @PatchMapping("/v1/free-comments/{commentId}")
    public void patchFreeComment(@PathVariable("commentId") Long freeCommentId,
                                 @RequestBody FreeCommentModifyDTO freeCommentModifyDTO) {

        freeCommentService.modifyFreeComment(freeCommentModifyDTO,freeCommentId);
    }

    @DeleteMapping("/v1/free-comments/{freeCommentId}")
    public void deleteFreeComment(@PathVariable("freeCommentId") Long freeCommentId) {
        freeCommentService.removeByCommentId(freeCommentId);
    }


}
