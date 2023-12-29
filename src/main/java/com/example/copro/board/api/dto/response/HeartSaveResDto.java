package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.MemberHeartBoard;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartSaveResDto {

    Long boardId;

    Long memberId;

    @Builder
    private HeartSaveResDto(Long boardId, Long memberId){
        this.boardId = boardId;
        this.memberId = memberId;
    }

    public static HeartSaveResDto of(MemberHeartBoard memberHeartBoard) {
        return HeartSaveResDto.builder()
                .boardId(memberHeartBoard.getBoard().getBoardId())
                .memberId(memberHeartBoard.getMember().getMemberId())
                .build();
    }

}
