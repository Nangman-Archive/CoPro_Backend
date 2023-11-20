package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Report;
import com.example.copro.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportResDto {

    Long boardId;

    Long memberId;

    String contents;

    @Builder
    private ReportResDto(Long boardId, Long memberId, String contents){
        this.boardId = boardId;
        this.memberId = memberId;
        this.contents = contents;
    }

    public static ReportResDto of(Report report) {
        return ReportResDto.builder()
                .boardId(report.getBoard().getBoardId())
                .memberId(report.getMember().getMemberId())
                .contents(report.getContents())
                .build();
    }


}
