package com.example.copro.member.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record BlockedMemberReqDto(
        @NotNull
        Long blockedMemberId
) {
}
