package com.example.finalapp.service.board.free.comment;

import com.example.finalapp.dto.board.PageRequestDTO;
import com.example.finalapp.dto.board.SliceDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentListDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentModifyDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentWriteDTO;
import com.example.finalapp.mapper.board.free.comment.FreeCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeCommentService {
    private final FreeCommentMapper freeCommentMapper;

    // 댓글 추가를 위해 write DTO를 받아서 mapper 메서드에 전달함
    public void addFreeComment(FreeCommentWriteDTO freeCommentWriteDTO,
                               Long boardId,
                               Long memberId) {

        freeCommentWriteDTO.setFreeBoardId(boardId);
        freeCommentWriteDTO.setMemberId(memberId);

        freeCommentMapper.insertComment(freeCommentWriteDTO);
    }

    public List<FreeCommentListDTO> getFreeComments(Long boardId) {
        return freeCommentMapper.selectListByBoardId(boardId);
    }

    public int getTotalByBoardId(Long boardId) {
        return freeCommentMapper.selectTotalByBoardId(boardId);
    }

    public void modifyFreeComment(FreeCommentModifyDTO commentModifyDTO,
                                  Long commentId){
        commentModifyDTO.setFreeCommentId(commentId);
        freeCommentMapper.updateFreeComment(commentModifyDTO);
    }

    public void removeByCommentId(Long commentId) {
        freeCommentMapper.deleteByCommentId(commentId);
    }

    public SliceDTO<FreeCommentListDTO> getFreeCommentsByBoardIdWithSlice(Long freeBoardId,
                                                  PageRequestDTO pageRequestDTO) {
        List<FreeCommentListDTO> commentList = freeCommentMapper.selectByBoardIdWithSlice(freeBoardId, pageRequestDTO);
        return new SliceDTO<>(pageRequestDTO.getPage(),
                pageRequestDTO.getSize(),
                commentList);
    }

}
