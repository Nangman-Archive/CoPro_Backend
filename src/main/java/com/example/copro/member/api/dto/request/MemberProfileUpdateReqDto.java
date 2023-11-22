package com.example.copro.member.api.dto.request;

public record MemberProfileUpdateReqDto(
        String occupation,
        String language,
        String career,
        String gitHubUrl
) {
}
