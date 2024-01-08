package com.example.copro.member.api.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record MemberInfoResDto (
        int myViewType,
        Page<MemberResDto> memberResDto
){
    public static MemberInfoResDto of(int myViewType, Page<MemberResDto> memberResDto) {
        return MemberInfoResDto.builder()
                .myViewType(myViewType)
                .memberResDto(memberResDto)
                .build();
    }
}
