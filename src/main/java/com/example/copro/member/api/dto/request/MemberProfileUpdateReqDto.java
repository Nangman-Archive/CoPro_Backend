package com.example.copro.member.api.dto.request;

public record MemberProfileUpdateReqDto(
        String nickName,
        String occupation,
        String language,
        String career
) {
}
