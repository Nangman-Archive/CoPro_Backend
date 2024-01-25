package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.MemberHeartBoard;
import lombok.Builder;

@Builder
public record HeartSaveResDto(
    int heart
) {
    public static HeartSaveResDto of(Board board) {
        return HeartSaveResDto.builder()
                .heart(board.getHeart())
                .build();
    }

}
