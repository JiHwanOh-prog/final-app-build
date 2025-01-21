package com.example.finalapp.service.sms;


import com.example.finalapp.exception.sms.SmsException;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsService {
    private DefaultMessageService messageService;

    // api키 같은 중요한 정보들은 코드상에 함부로 노출시키면 안된다.(git-hub에서 털림)
    // application.properties는 gitignore에 등록하는 파일이므로 해당 파일에 중요한 설정값을 저장
    // api키 들도 프로퍼티 파일에 일종의 변수로 저장하여 사용한다.

    // 스프링에서 지원하는 @Value 어노테이션으로 프로퍼티의 값을 필드에 넣을 수 있다.
    // @Value("${프로퍼티이름}")
    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;
    @Value("${coolsms.sender.number}")
    private String senderNumber;

    // @Value를 사용한 경우, 생성자에서 해당 필드를 사용할 수 없다.
    // 스프링 빈 객체는 생성자를 통해 만들어지는데 @Value로 값을 넣는 시점은 객체가 만들어진 이후이다.

//    public SmsService() {
//        messageService = NurigoApp
//                .INSTANCE
//                .initialize(apiKey,apiSecret,"https://api.coolsms.co.kr");
//    }

    // @PostConstruct : 생성자 실행 직후에 실행할 메서드를 지정하는 것
    @PostConstruct
    public void init(){
        messageService = NurigoApp
                .INSTANCE
                .initialize(apiKey,apiSecret,"https://api.coolsms.co.kr");
    }

    public String sendVerificationCode (String phoneNumber){
        String verificationCode = generateVerificationCode();
        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(phoneNumber);
        message.setText("[커뮤니티] 인증번호 : " + verificationCode);

        try {
            // 메시지 발송 처리
            this.messageService.sendOne(new SingleMessageSendingRequest(message));
        } catch (Exception e) {
            throw new SmsException("인증번호 전송 실패 : " + e);
        }

        // 사용자에게 발송한 인증코드를 반환
        return verificationCode;
    }

    private String generateVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(1000000);

        // code를 6자리로 포매팅하고, 6자리보다 작은 경우 0으로 채워서 포멧을 맞춰줌
        return String.format("%06d", code);
    }
}











