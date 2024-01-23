package com.example.copro.comment.api.dto.request;

public record CommentSaveReqDto(
        Long parentId,
        String content
) {
}
