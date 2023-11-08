package com.example.copro.board.api.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {

    private String title;

    private String category;
    private String contents;

    private String tag;

    private int count;

    public BoardRequestDto(String title, String category, String contents, String tag, int count) {
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
    }

}
