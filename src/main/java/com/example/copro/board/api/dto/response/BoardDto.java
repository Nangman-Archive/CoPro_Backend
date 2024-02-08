package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.image.api.dto.response.ImageBoardResDto;
import java.time.LocalDateTime;
import lombok.Builder;


@Builder // 이 객체 만드는 생성자도 있어야함
public record BoardDto (
        long id,
        String title,
        String nickName,
        LocalDateTime createAt,
        int count,
        int heart,
        String imageUrl,
        int commentCount
){ //안쪽에 dto쓰는 이유: 보수적인 작업을 위해(바깥쪽에선 이 dto안써짐)
    //빌더를 통해 밑에 생성자를 간접적으로 부를 수 있게 해줌, from을 사용하여 좀 더 명확한 메서드이름
     public static BoardDto from(Board board) {
        ImageBoardResDto image = ImageBoardResDto.from(board);

        return BoardDto.builder()
                .id(board.getBoardId())
                .title(board.getTitle())
                .nickName(board.getMember().getNickName())
                .createAt(board.getCreateAt())
                .count(board.getCount())
                .heart(board.getHeart())
                .imageUrl(image.imageUrl())
                .build();
    }

    public static BoardDto from(Board board, int commentCount) {
        ImageBoardResDto image = ImageBoardResDto.from(board);

        return BoardDto.builder()
                .id(board.getBoardId())
                .title(board.getTitle())
                .nickName(board.getMember().getNickName())
                .createAt(board.getCreateAt())
                .count(board.getCount())
                .heart(board.getHeart())
                .imageUrl(image.imageUrl())
                .commentCount(commentCount)
                .build();
    }

}
