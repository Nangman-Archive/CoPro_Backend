package com.example.copro.board.api.dto.response;

import com.example.copro.member.domain.MemberScrapBoard;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapSaveResDto {

    Long boardId;

    Long memberId;

    @Builder
    private ScrapSaveResDto(Long boardId, Long memberId){
        this.boardId = boardId;
        this.memberId = memberId;
    }

    public static ScrapSaveResDto of(MemberScrapBoard memberScrapBoard) {
        return ScrapSaveResDto.builder()
                .boardId(memberScrapBoard.getBoard().getBoardId())
                .memberId(memberScrapBoard.getMember().getMemberId())
                .build();
    }

}
