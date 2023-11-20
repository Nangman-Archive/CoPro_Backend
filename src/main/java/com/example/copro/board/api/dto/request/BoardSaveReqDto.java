package com.example.copro.board.api.dto.request;


import com.example.copro.board.domain.Category;
import com.example.copro.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardSaveReqDto {

    private String title;

    private Category category;
    private String contents;

    private String tag;

    private int count;

    private Long memberId;

    public BoardSaveReqDto(String title, Category category, String contents, String tag, int count, Long memberId) {
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
        this.memberId = memberId;
    }

}
