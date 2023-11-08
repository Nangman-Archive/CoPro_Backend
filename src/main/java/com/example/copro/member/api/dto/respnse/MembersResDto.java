package com.example.copro.member.api.dto.respnse;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MembersResDto {
    private List<MemberResDto> memberResDtos;

    public MembersResDto(List<MemberResDto> memberResDtos) {
        this.memberResDtos = memberResDtos;
    }
}
