package com.example.copro.member.mypage.api.dto.response;

import com.example.copro.member.domain.Member;
import lombok.Builder;

@Builder
public record DeleteAccountResDto(
        String email
) {
    public static DeleteAccountResDto from(Member member) {
        return DeleteAccountResDto.builder()
                .email(member.getEmail())
                .build();
    }
}
