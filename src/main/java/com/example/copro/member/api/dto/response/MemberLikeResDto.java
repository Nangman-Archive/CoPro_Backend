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
        String career,
        String gitHubUrl
) {
    public static MemberLikeResDto from(MemberLike memberLike) {
        return MemberLikeResDto.builder()
                .memberLikeId(memberLike.getLikedMember().getMemberId())
                .name(memberLike.getLikedMember().getName())
                .email(memberLike.getLikedMember().getEmail())
                .picture(memberLike.getLikedMember().getPicture())
                .occupation(memberLike.getLikedMember().getOccupation())
                .language(memberLike.getLikedMember().getLanguage())
                .career(memberLike.getLikedMember().getCareer())
                .gitHubUrl(memberLike.getLikedMember().getGitHubUrl())
                .build();
    }
}
