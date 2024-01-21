package com.example.copro.board.api.dto.response;

import com.example.copro.member.domain.MemberScrapBoard;
import lombok.Builder;

@Builder
public record ScrapSaveResDto(
        Long boardId,
        Long memberId

) {
    public static ScrapSaveResDto of(MemberScrapBoard memberScrapBoard) {
        return ScrapSaveResDto.builder()
                .boardId(memberScrapBoard.getBoard().getBoardId())
                .memberId(memberScrapBoard.getMember().getMemberId())
                .build();
    }

}
