package com.example.finalapp.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberJoinDTO {
    private Long memberId;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String zipCode;
    private String address;
    private String addressDetail;
}
