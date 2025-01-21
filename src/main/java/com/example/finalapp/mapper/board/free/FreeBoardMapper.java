package com.example.finalapp.mapper.board.free;

import com.example.finalapp.dto.board.PageDTO;
import com.example.finalapp.dto.board.PageRequestDTO;
import com.example.finalapp.dto.board.free.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FreeBoardMapper {
    void insertFreeBoard(FreeBoardWriteDTO boardWriteDTO);

    Optional<FreeBoardDetailDTO> selectById(Long freeBoardId);

    void updateViewCount(Long freeBoardId);

    List<FreeBoardListDTO> selectAllFreeBoards();

    void updateFreeBoard(FreeBoardModifyDTO freeBoardModifyDTO);

    void deleteFreeBoard(Long freeBoardId);

    List<FreeBoardListDTO> selectBySearchCondition(FreeBoardSearchDTO searchDTO);

    // 여러개의 객체를 받아야 하는 경우 @Param 어노테이션을 활용한다.
    // 이유는?? xml 에서 어느 객체에서 받는지 모르기때문에 이름을 지어준다고 생각하면 된다.
    List<FreeBoardListDTO> selectBySearchCondWithPage(@Param("cond") FreeBoardSearchDTO searchDTO,
                                                      @Param("page") PageRequestDTO pageRequestDTO);

    int countBySearchCondition(FreeBoardSearchDTO searchDTO);
}
