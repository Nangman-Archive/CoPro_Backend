package com.example.copro.member.api.dto.respnse;

import com.example.copro.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberResDto(
        String name,
        String email,
        String picture,
        String occupation,
        String language,
        String career

) {
    public static MemberResDto from(Member member) {
        return MemberResDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .picture(member.getPicture())
                .occupation(member.getOccupation())
                .language(member.getLanguage())
                .career(member.getCareer())
                .build();
    }
}
