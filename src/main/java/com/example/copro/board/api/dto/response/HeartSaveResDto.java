package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.MemberHeartBoard;
import lombok.Builder;

@Builder
public record HeartSaveResDto(
        Long boardId,
        Long memberId
) {
    public static HeartSaveResDto of(MemberHeartBoard memberHeartBoard) {
        return HeartSaveResDto.builder()
                .boardId(memberHeartBoard.getBoard().getBoardId())
                .memberId(memberHeartBoard.getMember().getMemberId())
                .build();
    }

}
