package com.example.copro.member.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BlockedMemberReqDto(
        @NotBlank
        String blockedMemberNickName
) {
}
