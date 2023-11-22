package com.example.copro.member.api.dto.respnse;

import com.example.copro.member.domain.MemberLike;
import lombok.Builder;

@Builder
public record MemberLikeResDto(
        Long memberLikeList
) {
    public static MemberLikeResDto from(MemberLike memberLike) {
        return MemberLikeResDto.builder()
                .memberLikeList(memberLike.getLikedMember().getMemberId())
                .build();
    }
}
