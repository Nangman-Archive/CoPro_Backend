package com.example.copro.member.api.dto.response;

import com.example.copro.member.domain.MemberLike;
import lombok.Builder;

@Builder
public record MemberLikeResDto(
        Long memberLikeId,
        String name,
        String email,
        String picture,
        String occupation,
        String language,
        int career,
        String gitHubUrl,
        boolean isLike,
        int likeMembersCount
) {
    public static MemberLikeResDto of(MemberLike memberLike, boolean isLike, int likeMembersCount) {
        return MemberLikeResDto.builder()
                .memberLikeId(memberLike.getLikedMember().getMemberId())
                .name(memberLike.getLikedMember().getNickName())
                .email(memberLike.getLikedMember().getEmail())
                .picture(memberLike.getLikedMember().getPicture())
                .occupation(memberLike.getLikedMember().getOccupation())
                .language(memberLike.getLikedMember().getLanguage())
                .career(memberLike.getLikedMember().getCareer())
                .gitHubUrl(memberLike.getLikedMember().getGitHubUrl())
                .isLike(isLike)
                .likeMembersCount(likeMembersCount)
                .build();
    }
}
