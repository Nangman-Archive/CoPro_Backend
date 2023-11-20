package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResDto {

    private Long boardId;
    private String title;

    private Category category;
    private String contents;

    private String tag;

    private int count;

    @Builder
    public BoardResDto(Long boardId, String title, Category category, String contents, String tag, int count) {
        this.boardId = boardId;
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
    }

    public static BoardResDto of(Board board) {
        return BoardResDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .category(board.getCategory())
                .contents(board.getContents())
                .tag(board.getTag())
                .count(board.getCount())
                .build();
    }

}
