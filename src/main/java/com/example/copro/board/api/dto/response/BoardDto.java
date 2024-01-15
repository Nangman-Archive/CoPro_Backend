package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.image.api.dto.response.DefaultImage;
import com.example.copro.image.api.dto.response.ImageBoardResDto;
import com.example.copro.image.domain.Image;
import com.example.copro.member.domain.MemberScrapBoard;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder // 이 객체 만드는 생성자도 있어야함
class BoardDto { //안쪽에 dto쓰는 이유: 보수적인 작업을 위해(바깥쪽에선 이 dto안써짐)
    long id;
    String title;
    String nickname;
    LocalDateTime createAt;
    int count;
    int heart;
    String imageUrl;


    //빌더를 통해 밑에 생성자를 간접적으로 부를 수 있게 해줌, from을 사용하여 좀 더 명확한 메서드이름
     public static BoardDto from(Board board) {

            /*String imageUrl = null;
            if (!board.getImages().isEmpty()) { // 이미지가 존재하면
                Image image = board.getImages().get(0); // 첫 번째 이미지를 가져옴
                imageUrl = image.getImageUrl(); // 이미지 URL을 가져옴
            } else {
                imageUrl = ConstantClass.getInstance().getDefaultImageUrl();
            }*/
        ImageBoardResDto image = ImageBoardResDto.of(board);

        return BoardDto.builder()
                .id(board.getBoardId())
                .title(board.getTitle())
                .nickname(board.getMember().getNickName())
                .createAt(board.getCreateAt())
                .count(board.getCount())
                .heart(board.getHeart())
                .imageUrl(image.imageUrl())
                .build();
    }

     static BoardDto memberScrapBoardFrom(MemberScrapBoard memberScrapBoard) {
         String imageUrl = null;

        if (!memberScrapBoard.getBoard().getImages().isEmpty()) { // 이미지가 존재하면
            Image image = memberScrapBoard.getBoard().getImages().get(0); // 첫 번째 이미지를 가져옴
            imageUrl = image.getImageUrl(); // 이미지 URL을 가져옴
        } else {
            imageUrl = DefaultImage.DEFAULT_IMAGE.name();
        }

        return BoardDto.builder()
                .id(memberScrapBoard.getBoard().getBoardId())
                .title(memberScrapBoard.getBoard().getTitle())
                .imageUrl(imageUrl)
                .build();
    }

        /*//board의 리스트를 받아서 dto의 리스트로 만들어 줘야한다.
        static List<BoardDto> from(Page<Board> boards) {
            return boards.stream() //컬렉션을 순회하며 어떤 작업을 컬렉션안의 모든 원소에 대해 처리
                    //.map(board -> BoardDto.from(board, board.getImageUrl())
                    .map(BoardDto::from) //map => 내부에 있는걸 바꿈 ,board를 받아 boarddto로 바꾸고
                    .toList(); //리스트로 반환환다
        }*/

     static List<BoardDto> memberScrapBoardFrom(Page<MemberScrapBoard> memberScrapBoards) {
        return memberScrapBoards.stream() //컬렉션을 순회하며 어떤 작업을 컬렉션안의 모든 원소에 대해 처리
                //.map(board -> BoardDto.from(board, board.getImageUrl())
                .map(BoardDto::memberScrapBoardFrom) //map => 내부에 있는걸 바꿈 ,board를 받아 boarddto로 바꾸고
                .toList(); //리스트로 반환환다
    }
}
