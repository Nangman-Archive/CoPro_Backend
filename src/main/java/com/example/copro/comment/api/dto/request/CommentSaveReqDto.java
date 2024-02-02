package com.example.copro.comment.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentSaveReqDto(
        @NotNull
        Long parentId,
        @NotBlank
        String content
) {
}
