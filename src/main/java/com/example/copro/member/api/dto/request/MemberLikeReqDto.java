package com.example.copro.member.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record MemberLikeReqDto(
        @NotNull
        Long likeMemberId
) {
}
