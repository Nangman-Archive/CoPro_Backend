package com.example.copro.board.api.dto.request;


import com.example.copro.board.domain.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSaveReqDto {

    private String title;

    private Category category;
    private String contents;

    private String tag;

    private int count;

    private int heart;

    private List<Long> imageId;

    public BoardSaveReqDto(String title, Category category, String contents, String tag, int count, int heart, List<Long> imageId) {
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
        this.heart = heart;
        this.imageId = imageId;
    }

}
