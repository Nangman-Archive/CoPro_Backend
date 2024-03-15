package com.example.copro.member.mypage.api.dto.response;

import com.example.copro.member.domain.Member;
import lombok.Builder;

@Builder
public record WithdrawalResDto(
        String email
) {
    public static WithdrawalResDto from(Member member) {
        return WithdrawalResDto.builder()
                .email(member.getEmail())
                .build();
    }
}
