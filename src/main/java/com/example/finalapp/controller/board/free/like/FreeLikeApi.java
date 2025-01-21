package com.example.finalapp.controller.board.free.like;

import com.example.finalapp.service.board.free.like.FreeLikeService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FreeLikeApi {
    private final FreeLikeService freeLikeService;

    // 내부 클래스 사용
    @Getter @Setter @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LikeResponse {
        private boolean liked;
        private int likeCount;
        private String message;
        private boolean success;

        // 1. service에 종속되어 있는 객체를 사용하기에 결합도가 높아진다.
        // 유지보수성이 떨어진다.
//        public static LikeResponse error(FreeLikeService.FreeLikeResult freeLikeResult, String message) {
//            return new LikeResponse(freeLikeResult.isLiked(), freeLikeResult.getLikeCount(), message, false);
//        }

        // 2. 결합도가 떨어져서 유지보수성이 향상된다.
        // 내부 클래스를 사용하여 독립적으로 사용됨.
        // 이번엔 이 코드로 진행함.
        public static LikeResponse error(boolean liked, int likeCount, String message) {
            return new LikeResponse(liked, likeCount, message, false);
        }

        public static LikeResponse success(boolean liked, int likeCount, String message) {
            return new LikeResponse(liked, likeCount, message, true);
        }
    }

    @GetMapping("/v1/free-boards/{freeBoardId}/likes/check")
    public LikeResponse checkLikeStatus(@PathVariable("freeBoardId") Long freeBoardId,
                                @SessionAttribute(value = "memberId", required = false) Long memberId) {

        FreeLikeService.FreeLikeResult result = freeLikeService.isLikedByMember(memberId,freeBoardId);

        return LikeResponse.success(result.isLiked(), result.getLikeCount(), "확인 완료");
    }

    @PutMapping("/v1/free-boards/{freeBoardId}/likes")
    public LikeResponse putLike(@PathVariable("freeBoardId") Long freeBoardId,
                                @SessionAttribute(value = "memberId", required = false) Long memberId){
        if (memberId == null) {
            FreeLikeService.FreeLikeResult result = freeLikeService.isLikedByMember(memberId, freeBoardId);
            return LikeResponse.error(result.isLiked(), result.getLikeCount(), "로그인이 필요한 서비스입니다.");
        }
        FreeLikeService.FreeLikeResult result = freeLikeService.toggleLike(memberId,freeBoardId);
        return LikeResponse.success(result.isLiked(), result.getLikeCount(), "좋아요 처리되었습니다.");
    }

}
