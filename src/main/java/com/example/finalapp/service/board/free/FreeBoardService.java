package com.example.finalapp.service.board.free;

import com.example.finalapp.dto.board.PageDTO;
import com.example.finalapp.dto.board.PageRequestDTO;
import com.example.finalapp.dto.board.free.*;
import com.example.finalapp.dto.board.free.file.FreeFileDTO;
import com.example.finalapp.exception.board.BoardNotFoundException;
import com.example.finalapp.mapper.board.free.FreeBoardMapper;
import com.example.finalapp.mapper.board.free.file.FreeFileMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional // 스프링은 트랜젝션 어노테이션을 하면 트렌젝션 처리를 해준다. 클래스 뿐만 아니라 메서드에도 붙힐 수 있다.
@RequiredArgsConstructor
public class FreeBoardService {
    private final FreeBoardMapper freeBoardMapper;
    private final FreeFileMapper freeFileMapper;

    @Value("${free.file.upload-path}")
    private String uploadPath;

    // 게시글 추가 기능
    public void addFreeBoard(FreeBoardWriteDTO boardWriteDTO, Long memberId) {
        boardWriteDTO.setMemberId(memberId);

        freeBoardMapper.insertFreeBoard(boardWriteDTO);
    }

    public FreeBoardDetailDTO getFreeBoardById(Long boardId) {
        freeBoardMapper.updateViewCount(boardId);

        return freeBoardMapper.selectById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("자유게시판 글을 찾을 수 없음, ID : " + boardId));
    }

    public List<FreeBoardListDTO> getFreeBoardList() {
        return freeBoardMapper.selectAllFreeBoards();
    }

    public void addFreeBoardWithFile(FreeBoardWriteDTO boardWriteDTO,
                                     Long memberId,
                                     MultipartFile multipartFile) throws IOException {
        // 1. 게시글 저장
        boardWriteDTO.setMemberId(memberId);
        freeBoardMapper.insertFreeBoard(boardWriteDTO);

        // 2. 파일 존재 여부 검사
        // 앞에서 null인지 먼저 검사하고 파일 존재여부 확인
        // or연산자는 앞에 true면 뒤에는 연산을 안하기 때문에!
        if (multipartFile == null || multipartFile.isEmpty()) { return; }

        // 2-1. 파일이 존재한다면, 파일 정보 가져오기
        String originalFilename = multipartFile.getOriginalFilename();// 원본파일 이름.확장자 가 이름
        // sample.01.jpg : .jpg를 분리하여 확장자를 별도 저장
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 확장자 분리, 뒤에서부터 substring을 사용하여 분리
        String uuid = UUID.randomUUID().toString();// uuid (파일 이름 중복을 방지하기 위해 사용)
        // 파일을 저장할 전체 경로
        // c:/upload/free/yyyy/MM/dd/파일명.확장자
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String savePath = this.uploadPath + "/" + datePath;// 실제 저장될 전체 디렉터리 경로
        // uploadPath : c:/upload/free
        // datePath : yyyy/MM/dd/
        // savePath : c:/upload/free/yyyy/MM/dd 이부분이 savePath

        FreeFileDTO freeFileDTO = new FreeFileDTO();
        freeFileDTO.setOriginalFilename(originalFilename);
        freeFileDTO.setExtension(extension);
        freeFileDTO.setUuid(uuid);
        freeFileDTO.setFilePath(datePath);
        freeFileDTO.setFreeBoardId(boardWriteDTO.getFreeBoardId());

        log.debug("freeFileDTO: {}", freeFileDTO);

        // 3-1. 서버 컴퓨터에 실제 파일 저장 처리 (파일 입출력 활용)
        File uploadDir = new File(savePath); // 저장하려는 경로 (디렉토리만 포함하는 경로)

        // 만약 저장하려는 경로가 존재하지 않다면????
        if (!uploadDir.exists()){
            // 경로까지 필요한 모든 디렉터리 생성!!
            uploadDir.mkdirs(); //mkdirs(): make directories 의 약자
        }

        // 실제 저장할 파일의 이름을 uuid로 사용
        String fileSystemName = uuid + extension;
        // fileSystemName : 서버에 저장할 파일 이름, uuid, 확장자
        String fileFullpath = savePath + "/" + fileSystemName;
        // filefullpath : C:/upload/free/yyyy/MM/dd/uuid.확장자
        File file = new File(fileFullpath);

        // 실제 파일 저장하기(실제 저장 처리는 이 한줄이 끝)
        multipartFile.transferTo(file);

        // 썸네일 저장

        String contentType = Files.probeContentType(file.toPath());
        // 이미지 파일인 경우에만 처리하는 조건식
        if (contentType.startsWith("image")) {
            Thumbnails.of(file)
                    .size(300,200)
                    .toFile(new File(savePath + "/th_" + fileSystemName));
            // 썸네일은 같은 경로상에 파일 이름만 th_를 붙여 사용
        }

        // --------------

        // 3-2. 저장한 실제파일 정보를 DB에 삽입
        freeFileMapper.insertFile(freeFileDTO);

        /*
        이 메서드는 총 2번의 Insert를 수행하는 메서드이다.
        만약 첫번째 insert만 실행되고 두번째 오류가 발생한다면, 둘 다 롤백되어야 데이터 무결성을 지킬 수 있다.
        이를 위해서 트랜잭션 처리를 해줘야한다.
        (SELECT를 제외한 모든 DML은 여러번 사용했을 때 하나의 트랜잭션이 필요)
        스프링에서 트랜잭션 처리는 어노테이션으로 매우 쉽게 처리 가능하다.

        ** 주의 사항 **
        하나의 서비스 클래스에서 내부에 존재하는 다른 메서드를 호출하면 트랜잭션이 적용되지 않는다.
        (내부 호출 적용 안됨!!!!!)
        
        예 ) 
        BoardService에 a메서드와 b메서드가 존재할 때
        a메서드의 쿼리와 b메서드의 쿼리는 다른 트랜잭션을 사용한다.
        a메서드 내부에서 b메서드를 호출하면 내부 호출이므로, 별도의 트랜잭션이 적용된다.
        (같은 클래스의 메서드끼리 호출하면 안됨!!)
         */
    }

    //  글 수정
    public void modifyFreeBoardWithFile(FreeBoardModifyDTO boardModifyDTO,
                                        MultipartFile multipartFile) throws IOException {

        // 글 수정
        freeBoardMapper.updateFreeBoard(boardModifyDTO);

        if (multipartFile == null || multipartFile.isEmpty()) {
            return;
        }

        // 파일이 존재하면 삭제 후 다시 저장 처리
        // DB에서 삭제
        freeFileMapper.deleteByBoardId(boardModifyDTO.getFreeBoardId());

        // 새 파일로 저장
        String originalFilename = multipartFile.getOriginalFilename();// 원본파일 이름.확장자 가 이름
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 확장자 분리, 뒤에서부터 substring을 사용하여 분리
        String uuid = UUID.randomUUID().toString();// uuid (파일 이름 중복을 방지하기 위해 사용)
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String savePath = this.uploadPath + "/" + datePath;// 실제 저장될 전체 디렉터리 경로

        FreeFileDTO freeFileDTO = new FreeFileDTO();
        freeFileDTO.setOriginalFilename(originalFilename);
        freeFileDTO.setExtension(extension);
        freeFileDTO.setUuid(uuid);
        freeFileDTO.setFilePath(datePath);
        freeFileDTO.setFreeBoardId(boardModifyDTO.getFreeBoardId());

        log.debug("freeFileDTO: {}", freeFileDTO);

        File uploadDir = new File(savePath); // 저장하려는 경로 (디렉토리만 포함하는 경로)

        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); //mkdirs(): make directories 의 약자
        }

        String fileSystemName = uuid + extension;
        String fileFullpath = savePath + "/" + fileSystemName;
        File file = new File(fileFullpath);

        multipartFile.transferTo(file);


        String contentType = Files.probeContentType(file.toPath());
        if (contentType.startsWith("image")) {
            Thumbnails.of(file)
                    .size(300, 200)
                    .toFile(new File(savePath + "/th_" + fileSystemName));
        }

        freeFileMapper.insertFile(freeFileDTO);
    }

    public void removeFreeBoard(Long boardId){
        freeBoardMapper.deleteFreeBoard(boardId);
    }

    public List<FreeBoardListDTO> getFreeBoardBySearchCondition(FreeBoardSearchDTO searchDTO){
        return freeBoardMapper.selectBySearchCondition(searchDTO);
    }

    // 페이지 처리 테스트는 통합 테스트로 진행하는 편이 좋다.
    // 단위 테스트로 하게 되면 삽입을 엄청 많이 해야해서 번거로울 수 있다.
    public PageDTO<FreeBoardListDTO> getFreeBoardsBySearchCondWithPage(FreeBoardSearchDTO searchDTO,
                                                  PageRequestDTO pageRequestDTO){
        List<FreeBoardListDTO> boardList = freeBoardMapper.selectBySearchCondWithPage(searchDTO, pageRequestDTO);
        int total = freeBoardMapper.countBySearchCondition(searchDTO);

        return new PageDTO<>(pageRequestDTO.getPage(),
                pageRequestDTO.getSize(),
                total,
                boardList);
    }
}
