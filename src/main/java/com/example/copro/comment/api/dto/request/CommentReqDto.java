package com.example.copro.comment.api.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReqDto {
    private Long memberId;
    private Long parentId;
    private String content;

    public CommentReqDto(String content) {
        this.content = content;
    }
}
