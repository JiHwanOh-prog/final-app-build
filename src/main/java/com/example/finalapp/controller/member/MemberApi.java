package com.example.finalapp.controller.member;

import com.example.finalapp.service.member.MemberService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApi {
    private final MemberService memberService;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)//생성자 접근 제한자 설정
    static class LoginIdCheckResponse {
        private String message;
        private boolean exists;

        public static LoginIdCheckResponse isDuplicated(String message) {
            return new LoginIdCheckResponse(message, true);
        }

        public static LoginIdCheckResponse isNotDuplicated(String message) {
            return new LoginIdCheckResponse(message, false);
        }
    }

    // 아이디 중복 검사
    // 중복 검사라는 행위를 나타내는 HTTP Method가 없기 때문에, 중복 여부를 조회한다는 의미로
    // GET을 사용 그러나 loginId를 조회하는 것이 아니라 존재 여부를 파악하는 것이므로
    // exists라는 동사를 사용(Restful 조금 위배)
    // 모든 api를 완벽하게 Rest 원칙만으로 만드는 것은 불가능하다.
    @GetMapping("/v1/members/loginId/exists")
    public LoginIdCheckResponse loginIdExists(String loginId) {
        log.info("아이디 중복 검사 : {}",loginId);

        return memberService.isLoginIdDuplicate(loginId) ?
                LoginIdCheckResponse.isDuplicated("이미 사용중인 아이디 입니다.") :
                LoginIdCheckResponse.isNotDuplicated("사용 가능한 아이디 입니다.");
    }
}
