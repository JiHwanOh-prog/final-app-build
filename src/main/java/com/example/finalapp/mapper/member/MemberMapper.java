package com.example.finalapp.mapper.member;

import com.example.finalapp.dto.member.MemberJoinDTO;
import com.example.finalapp.dto.member.MemberLoginDTO;
import com.example.finalapp.dto.member.MemberSessionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    void insertMember(MemberJoinDTO memberJoinDTO);

    Optional<MemberSessionDTO> selectLoginInfo(MemberLoginDTO memberLoginDTO);

    int countByLoginId(String loginId);
}
