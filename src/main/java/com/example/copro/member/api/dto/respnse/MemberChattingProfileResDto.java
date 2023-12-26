package com.example.copro.member.api.dto.respnse;

import com.example.copro.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberChattingProfileResDto(
        String nickName,
        String picture,
        String occupation
) {
    public static MemberChattingProfileResDto from(Member member) {
        return MemberChattingProfileResDto.builder()
                .nickName(member.getNickName())
                .picture(member.getPicture())
                .occupation(member.getOccupation())
                .build();
    }
}
