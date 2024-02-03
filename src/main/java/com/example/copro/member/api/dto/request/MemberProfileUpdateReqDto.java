package com.example.copro.member.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberProfileUpdateReqDto(
        @NotBlank
        String nickName,
        @NotBlank
        String occupation,
        @NotBlank
        String language,
        @NotNull
        int career
) {
}
