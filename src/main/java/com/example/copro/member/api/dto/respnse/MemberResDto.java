package com.example.copro.member.api.dto.respnse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResDto {
    private String name;
    private String email;
    private String picture;

    public MemberResDto(String name, String email, String picture) {
        this.name = name;
        this.email = email;
        this.picture = picture;
    }
}
