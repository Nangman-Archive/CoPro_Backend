package com.example.copro.comment.api.dto.request;

public record CommentReqDto(
        Long parentId,
        String content
) {
}
