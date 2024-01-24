package com.example.copro.member.api.dto.response;

import com.example.copro.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberResDto(
        Long memberId,
        String name,
        String email,
        String picture,
        String occupation,
        String language,
        String career,
        String gitHubUrl,
        String nickName,
        int likeMembersCount,
        boolean isLikeMembers

) {
    public static MemberResDto from(Member member) {
        return MemberResDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .picture(member.getPicture())
                .occupation(member.getOccupation())
                .language(member.getLanguage())
                .career(member.getCareer())
                .gitHubUrl(member.getGitHubUrl())
                .nickName(member.getNickName())
                .build();
    }

    public static MemberResDto of(Member member, int likeMembersCount, boolean isLikeMembers) {
        return MemberResDto.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .picture(member.getPicture())
                .occupation(member.getOccupation())
                .language(member.getLanguage())
                .career(member.getCareer())
                .gitHubUrl(member.getGitHubUrl())
                .nickName(member.getNickName())
                .likeMembersCount(likeMembersCount)
                .isLikeMembers(isLikeMembers)
                .build();
    }
}
