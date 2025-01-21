package com.example.finalapp.mapper.board.free.file;

import com.example.finalapp.dto.board.free.file.FreeFileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FreeFileMapper {
    void insertFile(FreeFileDTO freeFileDTO);

    Optional<FreeFileDTO> selectByBoardId(Long freeBoardId);

    void deleteByBoardId(Long freeBoardId);

    List<FreeFileDTO> selectOldFileList();

}
