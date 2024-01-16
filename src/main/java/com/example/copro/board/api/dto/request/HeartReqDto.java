package com.example.copro.board.api.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartReqDto {
    Long boardId;


    public HeartReqDto(Long boardId){
        this.boardId = boardId;
    }
}
