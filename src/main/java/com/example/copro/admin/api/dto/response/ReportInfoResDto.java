package com.example.copro.admin.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.member.domain.Member;
import com.example.copro.report.domain.Report;
import lombok.Builder;

@Builder
public record ReportInfoResDto(
        String nickname,
        Long boardId,
        String boardTitle,
        String contents
) {
    public static ReportInfoResDto of(Member member, Board board, Report report) {
        return ReportInfoResDto.builder()
                .nickname(member.getNickName())
                .boardId(board.getBoardId())
                .boardTitle(board.getTitle())
                .contents(report.getContents())
                .build();
    }
}
