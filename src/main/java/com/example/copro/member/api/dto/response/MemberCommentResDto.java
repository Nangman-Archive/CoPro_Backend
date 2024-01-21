package com.example.copro.member.api.dto.response;

import lombok.Builder;

@Builder
public record MemberCommentResDto(
        String nickName,
        String occupation
) {
}
