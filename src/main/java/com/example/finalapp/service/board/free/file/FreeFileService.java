package com.example.finalapp.service.board.free.file;

import com.example.finalapp.dto.board.free.file.FreeFileDTO;
import com.example.finalapp.mapper.board.free.file.FreeFileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeFileService {
    private final FreeFileMapper freeFileMapper;

    @Value("${free.file.upload-path}")
    private String uploadPath;

    public void removeOldFile() throws IOException {
        List<FreeFileDTO> oldFileList = freeFileMapper.selectOldFileList();

        // File
        // 자바에서 외부 파일을 다룰 때 사용하는 타입
        // Path
        // 자바 7 이후에 도입된 현대적인 타입
        // File타입과 동일하게 외부 파일 다룰 수 있으며, File 보다 더 편한 인터페이스와 기능을 제공함

        // 생성 방식 (여러 경로를 조합하여 만들 때)
        // new File(경로1 + 경로2 + 경로3);
        // Path.of(경로1, 경로2, 경로3);

        List<Path> oldPathList = new ArrayList<>();

        // DB에 들어있는 파일정보를 기반으로 Path객체 리스트를 만든다.
        for (FreeFileDTO freeFileDTO : oldFileList) {
            Path path = Path.of(uploadPath, "/", freeFileDTO.getFilePath(), "/", freeFileDTO.getUuid(), freeFileDTO.getExtension());
            Path thPath = Path.of(uploadPath, "/", freeFileDTO.getFilePath(), "/th_", freeFileDTO.getUuid(), freeFileDTO.getExtension());
            oldPathList.add(path);
            oldPathList.add(thPath);
        }

        // 실제 업로드 경로상에 존재하는 파일 목록 불러오기
        // 1. 하루 전 날짜 구하기
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        String datePath = yesterday.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // 2. datePath 경로상에 있는 디렉토리를 불러오기
        Path directory = Path.of(uploadPath, "/", datePath);

        // Files 타입도 자바 7 이후에 도입된 유틸성 클래스
        // 파일을 다루는 여러 정적 메서드를 제공, 주로 Path와 함께 많이 사용한다.
        List<Path> unnecessayPathList = Files.list(directory)   // 특정 디렉토리의 모든 파일 목록을 읽어옴(반환타입이 스트림)
                .filter(path -> !oldFileList.contains(path)) // oldPathList에 포함된 파일이 아닌 것만 필터링하여 가져옴(DB에 존재하지 않는 파일 목록들만 골라옴)
                .toList(); // 리스트로 반환

        for (Path path : unnecessayPathList) {
            if (Files.exists(path)) { // 존재 여부 확인
                Files.delete(path);   // 삭제 처리
            }
        }
    }
}
