package com.example.finalapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration : 스프링 빈으로 등록하는 어노테이션, 설정 파일을 등록할 때 사용
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // WebMvcConfigurer는 SpringMVC의 설정을 Java 코드로 커스텀할 수 있도록 도와주는 인터페이스
    // 우리는 특정 URL 경로로 요청이 들어왔을 때 실제 서버 PC의 특정 파일의 위치와 연결지을 것이다.
    @Value("${free.file.upload-path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /upload/free/ 하위 경로는 모두를 의미
        String resourceUrlPath = "/upload/free/**";

        // 자원 위치 설정
        // file: 웹상에서 http: 쓰는것처럼 약속을 하는 것. 
        // 프로젝트 내부의 경로를 나타낼 때 classpath:를 사용하듯
        // 로컬 파일 경로는 file: 을 붙여야함
        String resourceLocation = "file:" + uploadPath + "/";

        // 업로드 등록 처리
        //
        registry.addResourceHandler(resourceUrlPath)
                .addResourceLocations(resourceLocation);
    }
}
