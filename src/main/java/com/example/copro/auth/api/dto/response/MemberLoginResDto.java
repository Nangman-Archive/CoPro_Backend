package com.example.copro.auth.api.dto.response;

import com.example.copro.member.domain.Member;
import lombok.Builder;

@Builder
public record MemberLoginResDto(
        Member findMember
) {
    public static MemberLoginResDto from(Member member) {
        return MemberLoginResDto.builder()
                .findMember(member)
                .build();
    }
}
