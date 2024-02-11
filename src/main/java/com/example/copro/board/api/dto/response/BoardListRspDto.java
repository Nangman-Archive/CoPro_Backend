package com.example.copro.board.api.dto.response;

import com.example.copro.board.api.common.PageInfoDto;
import com.example.copro.board.domain.Board;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

//직렬화 => 객체 데이터를 Json 형식으로 바꿈, getter내부에 있다. / 역직렬화시엔 기본생성자(noargsconstructor)도 필요
//객체 직접 생성해서 넘김
public record BoardListRspDto(
        List<BoardDto> boards,
        PageInfoDto pageInfo
) {
    //바깥에서 이 메서드를 통해 변환가능
    public static BoardListRspDto from(Page<Board> boards) {
        List<BoardDto> boardDtoList = boards.getContent().stream()
                .map(BoardDto::from)
                .collect(Collectors.toList());
        PageInfoDto pageInfoDto = PageInfoDto.from(boards);

        return new BoardListRspDto(boardDtoList, pageInfoDto);
    }

    public static BoardListRspDto of(Page<BoardDto> boards) {
        List<BoardDto> boardDtoList = boards.getContent();
        PageInfoDto pageInfoDto = PageInfoDto.from(boards);

        return new BoardListRspDto(boardDtoList, pageInfoDto);
    }
}