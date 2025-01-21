package com.example.finalapp.mapper.board.free.like;

import com.example.finalapp.dto.board.free.like.FreeLikeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FreeLikeMapper {
    void insertFreeLike(FreeLikeDTO freeLikeDTO);

    int countByMemberIdAndBoardId(@Param("memberId") Long memberId,
                                  @Param("freeBoardId") Long freeBoardId);

    int countByBoardId(Long freeBoardId);

    void deleteByMemberIdAndBoardId(@Param("memberId") Long memberId,
                                    @Param("freeBoardId") Long freeBoardId);
}
