package com.example.finalapp.mapper.board.free.comment;

import com.example.finalapp.dto.board.PageRequestDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentListDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentModifyDTO;
import com.example.finalapp.dto.board.free.comment.FreeCommentWriteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FreeCommentMapper {
    void insertComment(FreeCommentWriteDTO freeCommentWriteDTO);

    List<FreeCommentListDTO> selectListByBoardId(Long freeBoardId);

    int selectTotalByBoardId(Long freeBoardId);

    void updateFreeComment(FreeCommentModifyDTO freeCommentModifyDTO);

    void deleteByCommentId(Long freeCommentId);

    List<FreeCommentListDTO> selectByBoardIdWithSlice(@Param("freeBoardId") Long freeBoardId,
                                                      @Param("page") PageRequestDTO pageRequestDTO);
}
