package com.example.finalapp.service.board.free.like;

import com.example.finalapp.dto.board.free.like.FreeLikeDTO;
import com.example.finalapp.mapper.board.free.like.FreeLikeMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeLikeService {
    private final FreeLikeMapper freeLikeMapper;

    @Getter @ToString @AllArgsConstructor
    public static class FreeLikeResult{
        // 좋아요를 눌렀는지 확인하는 liked 변수
        private boolean liked;
        // 좋아요 갯수
        private int likeCount;
    }

    // 1. 현재 로그인한 회원이 현재 글을 좋아요 눌렀었는지 유무를 반환 한다. ( 현재 좋아요 수 포함!)
    public FreeLikeResult isLikedByMember(Long memberId, Long freeBoardId) {
        int countExists = freeLikeMapper.countByMemberIdAndBoardId(memberId, freeBoardId);
        int countLike = freeLikeMapper.countByBoardId(freeBoardId);

        return new FreeLikeResult(countExists > 0 , countLike);
    }

    // 2. 현재 회원이 좋아요 버튼을 누르면 유/무에 따라 좋아요 삽입/삭제 처리
    public FreeLikeResult toggleLike(Long memberId, Long freeBoardId) {
        // 1. 좋아요 상태 확인
        int countExists = freeLikeMapper.countByMemberIdAndBoardId(memberId, freeBoardId);
        boolean isLiked = countExists > 0;
        
        // 2. 좋아요 상태 변경
        if (isLiked) {
            // 이미 좋아요 상태이므로 취소 처리 진행
            freeLikeMapper.deleteByMemberIdAndBoardId(memberId, freeBoardId);
        } else {
            // 좋아요 상태가 아니면 삽입 진행
            FreeLikeDTO freeLikeDTO = new FreeLikeDTO();
            freeLikeDTO.setMemberId(memberId);
            freeLikeDTO.setFreeBoardId(freeBoardId);
            freeLikeMapper.insertFreeLike(freeLikeDTO);
        }
        
        // 3. 변경된 후의 전체 좋아요 수 다시 조회
        int updatedCount = freeLikeMapper.countByBoardId(freeBoardId);

        return new FreeLikeResult(!isLiked, updatedCount);
    }
}



