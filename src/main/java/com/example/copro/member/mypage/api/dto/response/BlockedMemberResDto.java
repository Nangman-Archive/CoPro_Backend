package com.example.copro.member.mypage.api.dto.response;

import com.example.copro.member.domain.BlockedMemberMapping;
import lombok.Builder;

@Builder
public record BlockedMemberResDto(
        Long blockedMemberId,
        String name,
        String email,
        String picture,
        String occupation,
        String language,
        int career,
        String gitHubUrl,
        boolean isBlocked
) {
    public static BlockedMemberResDto of (BlockedMemberMapping blockedMemberMapping, boolean isBlocked) {
        return BlockedMemberResDto.builder()
                .blockedMemberId(blockedMemberMapping.getBlockedMember().getMemberId())
                .name(blockedMemberMapping.getBlockedMember().getNickName())
                .email(blockedMemberMapping.getBlockedMember().getEmail())
                .picture(blockedMemberMapping.getBlockedMember().getPicture())
                .occupation(blockedMemberMapping.getBlockedMember().getOccupation())
                .language(blockedMemberMapping.getBlockedMember().getLanguage())
                .career(blockedMemberMapping.getBlockedMember().getCareer())
                .gitHubUrl(blockedMemberMapping.getBlockedMember().getGitHubUrl())
                .isBlocked(isBlocked)
                .build();
    }
}
