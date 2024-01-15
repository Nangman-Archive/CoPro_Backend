package com.example.copro.board.api.dto.response;

import com.example.copro.board.api.common.ConstantClass;
import com.example.copro.board.api.common.PageInfoDto;
import com.example.copro.board.domain.Board;
import com.example.copro.image.api.dto.response.ImageBoardResDto;
import com.example.copro.image.domain.Image;
import com.example.copro.member.domain.MemberScrapBoard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

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
        List<BoardDto> boardDtoList = boards.getContent().stream()
                .map(BoardDto::from)
                .collect(Collectors.toList());
        PageInfoDto pageInfoDto = PageInfoDto.from(boards);

        return new BoardListRspDto(boardDtoList, pageInfoDto);
    }


    public static BoardListRspDto memberScrapBoardFrom(Page<MemberScrapBoard> memberScrapBoards) {
        List<BoardDto> boardDtoList = BoardDto.memberScrapBoardFrom(memberScrapBoards);
        PageInfoDto pageInfoDto = PageInfoDto.from(memberScrapBoards);

        return new BoardListRspDto(boardDtoList, pageInfoDto);
    }
}
