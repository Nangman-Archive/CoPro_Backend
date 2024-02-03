package com.example.copro.member.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberGitHubUrlUpdateReqDto(
        @NotBlank
        String gitHubUrl
) {
}
