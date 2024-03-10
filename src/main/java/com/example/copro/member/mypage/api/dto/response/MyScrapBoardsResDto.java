package com.example.copro.member.mypage.api.dto.response;

import com.example.copro.image.api.dto.response.ImageBoardResDto;
import com.example.copro.member.domain.MemberScrapBoard;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MyScrapBoardsResDto(
        Long boardId,
        String category,
        String title,
        String nickName,
        LocalDateTime createAt,
        int count,
        int heart,
        String imageUrl,
        int commentCount
) {
    public static MyScrapBoardsResDto of(MemberScrapBoard myBoard, int commentCount) {
        ImageBoardResDto image = ImageBoardResDto.from(myBoard.getBoard());

        return MyScrapBoardsResDto.builder()
                .boardId(myBoard.getBoard().getBoardId())
                .category(String.valueOf(myBoard.getBoard().getCategory()))
                .title(myBoard.getBoard().getTitle())
                .nickName(myBoard.getBoard().getMember().getNickName())
                .createAt(myBoard.getBoard().getCreateAt())
                .count(myBoard.getBoard().getCount())
                .heart(myBoard.getBoard().getHeart())
                .imageUrl(image.imageUrl())
                .commentCount(commentCount)
                .build();
    }
}
