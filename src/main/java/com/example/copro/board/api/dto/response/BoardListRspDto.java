package com.example.copro.board.api.dto.response;

import com.example.copro.board.api.common.PageInfoDto;
import com.example.copro.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardListRspDto {

    List<BoardDto> boards;

    PageInfoDto pageInfo;

    public BoardListRspDto(List<BoardDto> boards, PageInfoDto pageInfo) {
        this.boards = boards;
        this.pageInfo = pageInfo;
    }

    public static BoardListRspDto from(Page<Board> boards){
        List<BoardDto> boardDtoList = BoardDto.from(boards);
        PageInfoDto pageInfoDto = PageInfoDto.from(boards);
        return new BoardListRspDto(boardDtoList, pageInfoDto);
    }

    @Getter
    @Builder
    static class BoardDto {
        long id;
        String title;

        static BoardDto from(Board board) {
            return BoardDto.builder()
                    .id(board.getBoardId())
                    .title(board.getTitle())
                    .build();
        }

        static List<BoardDto> from(Page<Board> boards) {
            return boards.stream()
                    .map(board -> BoardDto.from(board))
                    .collect(Collectors.toList());
        }
    }

}
