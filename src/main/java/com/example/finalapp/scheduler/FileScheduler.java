package com.example.finalapp.scheduler;


import com.example.finalapp.service.board.free.file.FreeFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileScheduler {
    private final FreeFileService freeFileService;

//    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(cron = "0/10 * * * * *")
    public void deleteOldFiles() {
        log.info("불필요한 이미지 파일 삭제 진행");

        try {
            freeFileService.removeOldFile();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("불필요한 이미지 파일 삭제 완료!!!");
    }
}
