package com.example.copro.member.mypage.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateViewTypeReqDto (
        @NotNull
        int viewType
) {
}
