package com.example.copro.comment.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentSaveReqDto(
        Long parentId,
        @NotBlank
        String content
) {
}
