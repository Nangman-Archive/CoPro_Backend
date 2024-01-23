package com.example.copro.comment.api.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentBoardResDto {
    private Long commentCount;

    public CommentBoardResDto(Long commentCount) {
        this.commentCount = commentCount;
    }

    public static CommentBoardResDto from(Long commentCount) {
        return new CommentBoardResDto(commentCount);
    }
}
