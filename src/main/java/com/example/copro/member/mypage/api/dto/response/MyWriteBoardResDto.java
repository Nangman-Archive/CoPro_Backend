package com.example.copro.member.mypage.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.image.api.dto.response.ImageBoardResDto;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MyWriteBoardResDto (
        Long boardId,
        String category,
        String title,
        String nickName,
        LocalDateTime createAt,
        int count,
        int heart,
        String imageUrl,
        int commentCount
){
    public static MyWriteBoardResDto of(Board board, int commentCount) {
        ImageBoardResDto image = ImageBoardResDto.from(board);

        return MyWriteBoardResDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .category(String.valueOf(board.getCategory()))
                .nickName(board.getMember().getNickName())
                .createAt(board.getCreateAt())
                .count(board.getCount())
                .heart(board.getHeart())
                .imageUrl(image.imageUrl())
                .commentCount(commentCount)
                .build();
    }

}
