package com.example.finalapp.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberLoginDTO {
    private String loginId;
    private String password;
}
