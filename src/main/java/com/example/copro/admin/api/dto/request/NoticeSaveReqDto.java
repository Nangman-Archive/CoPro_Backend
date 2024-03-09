package com.example.copro.admin.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NoticeSaveReqDto(
        @NotBlank
        String title,
        @NotBlank
        String contents
) {
}
