package com.example.copro.member.api.dto.response;

import com.example.copro.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCommentResDto {
    private String nickName;
    private String occupation;

    public MemberCommentResDto(Member member) {
        this.nickName = member.getNickName();
        this.occupation = member.getOccupation();
    }

    @Builder
    public MemberCommentResDto(String nickName, String occupation){
        this.nickName = nickName;
        this.occupation = occupation;
    }
}
