package com.example.copro.board.api.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartReqDto {
    Long boardId;

    Long memberId;

    public HeartReqDto(Long boardId, Long memberId){
        this.boardId = boardId;
        this.memberId = memberId;

    }
}
