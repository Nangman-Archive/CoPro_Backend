package com.example.copro.board.api.dto.response;

import com.example.copro.board.api.common.PageInfoDto;
import com.example.copro.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter //직렬화 => 객체 데이터를 Json 형식으로 바꿈, getter내부에 있다. / 역직렬화시엔 기본생성자(noargsconstructor)도 필요
//객체 직접 생성해서 넘김
public class BoardListRspDto {

    List<BoardDto> boards;

    PageInfoDto pageInfo;

    public BoardListRspDto(List<BoardDto> boards, PageInfoDto pageInfo) {
        this.boards = boards;
        this.pageInfo = pageInfo;
    }

    //바깥에서 이 메서드를 통해 변환가능
    public static BoardListRspDto from(Page<Board> boards) {
        List<BoardDto> boardDtoList = BoardDto.from(boards);
        PageInfoDto pageInfoDto = PageInfoDto.from(boards);
        return new BoardListRspDto(boardDtoList, pageInfoDto);
    }

    @Getter
    @Builder // 이 객체 만드는 생성자도 있어야함
    static class BoardDto { //안쪽에 dto쓰는 이유: 보수적인 작업을 위해(바깥쪽에선 이 dto안써짐)
        long id;
        String title;

        //빌더를 통해 밑에 생성자를 간접적으로 부를 수 있게 해줌, from을 사용하여 좀 더 명확한 메서드이름
        static BoardDto from(Board board) {
            return BoardDto.builder()
                    .id(board.getBoardId())
                    .title(board.getTitle())
                    .build();
        }

        //board의 리스트를 받아서 dto의 리스트로 만들어 줘야한다.
        static List<BoardDto> from(Page<Board> boards) {
            return boards.stream() //컬렉션을 순회하며 어떤 작업을 컬렉션안의 모든 원소에 대해 처리
                    .map(board -> BoardDto.from(board)) //map => 내부에 있는걸 바꿈 ,board를 받아 boarddto로 바꾸고
                    .collect(Collectors.toList()); //리스트로 반환환다
        }

    }
}
