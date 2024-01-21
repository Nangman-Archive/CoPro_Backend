package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.image.api.dto.response.ImageBoardResDto;
import com.example.copro.member.domain.MemberScrapBoard;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;


@Builder // 이 객체 만드는 생성자도 있어야함
public record BoardDto (
        long id,
        String title,
        String nickName,
        LocalDateTime createAt,
        int count,
        int heart,
        String imageUrl
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

     static BoardDto memberScrapBoardFrom(MemberScrapBoard memberScrapBoard) {

        ImageBoardResDto image = ImageBoardResDto.from(memberScrapBoard.getBoard());

        return BoardDto.builder()
                .id(memberScrapBoard.getBoard().getBoardId())
                .title(memberScrapBoard.getBoard().getTitle())
                .imageUrl(image.imageUrl())
                .build();
    }

     static List<BoardDto> memberScrapBoardFrom(Page<MemberScrapBoard> memberScrapBoards) {
        return memberScrapBoards.stream() //컬렉션을 순회하며 어떤 작업을 컬렉션안의 모든 원소에 대해 처리
                .map(BoardDto::memberScrapBoardFrom) //map => 내부에 있는걸 바꿈 ,board를 받아 boarddto로 바꾸고
                .toList(); //리스트로 반환환다
    }
}
