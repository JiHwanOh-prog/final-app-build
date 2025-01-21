package com.example.finalapp.controller.sms;

import com.example.finalapp.service.sms.SmsService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsApi {
    private final SmsService smsService;

    @Getter @Setter @AllArgsConstructor
    public static class SmsApiResponse {
        private String message;
        private boolean success;

        public static SmsApiResponse success(String message) {
            return new SmsApiResponse(message, true);
        }

        public static SmsApiResponse error(String message) {
            return new SmsApiResponse(message, false);
        }

    }

    @Getter @Setter
    public static class SmsApiRequest {
        private String phoneNumber;
        private String smsCode;
    }

    @PostMapping("/verify")
    public SmsApiResponse sendVerificationCode(@RequestBody SmsApiRequest smsApiRequest,
                                     HttpSession session) {
        log.info("인증번호  발송할 번호 : {}", smsApiRequest.getPhoneNumber());

        try {
//            String verificationCode = smsService.sendVerificationCode(smsApiRequest.getPhoneNumber());
            String verificationCode = "123456";
            log.info("인증 번호 : {}", verificationCode);

            session.setAttribute("smsCode", verificationCode); // 세션에 인증번호 저장
            session.setAttribute("smsTime", LocalDateTime.now()); // 현재 시간(인증번호 유효 시간을 위해 저장)

            return SmsApiResponse.success("인증번호가 발송되었습니다.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return SmsApiResponse.error("오류가 발생했습니다.");
        }

    }

    @PostMapping("/verify/check")
    public SmsApiResponse checkVerificationCode (@RequestBody SmsApiRequest smsApiRequest,
                                       HttpSession session) {
        String savedCode = (String) session.getAttribute("smsCode");
        LocalDateTime savedTime = (LocalDateTime) session.getAttribute("smsTime");

        if (savedCode == null || savedTime == null) {
            return SmsApiResponse.error("인증번호를 먼저 요청해주세요");
        }

        // ChronoUnit : 시간 단위를 나타내는 enum(열거형), 내부에 시/분/초/일/주/월/년 등 여러 상수들이 존재
        // 해당 상수 객체를 활용하여 시간 계산을 편하게 할 수 있다.
        // ChronoUnit.MINUTES.between(시간,시간) : 두 시간의 차이를 분 단위로 계산
        if (ChronoUnit.MINUTES.between(savedTime, LocalDateTime.now()) >= 3){
            return SmsApiResponse.error("인증번호가 만료되었습니다.");
        }

        if(!savedCode.equals(smsApiRequest.getSmsCode())) {
            return SmsApiResponse.error("인증 번호가 일치하지 않습니다.");
        }

        // 인증 성공 시 세션에서 인증 관련 정보 제거
        session.removeAttribute("smsCode");
        session.removeAttribute("smsTime");

        return SmsApiResponse.success("인증이 완료되었습니다.");
    }
}




