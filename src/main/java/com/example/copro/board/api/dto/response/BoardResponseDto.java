package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {

    private Long boardId;
    private String title;

    private String category;
    private String contents;

    private String tag;

    private int count;

    @Builder
    public BoardResponseDto(Long boardId, String title, String category, String contents, String tag, int count) {
        this.boardId = boardId;
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
    }

    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .category(board.getCategory())
                .contents(board.getContents())
                .tag(board.getTag())
                .count(board.getCount())
                .build();
    }

}
