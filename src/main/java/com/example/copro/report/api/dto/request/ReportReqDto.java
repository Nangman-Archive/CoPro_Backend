package com.example.copro.report.api.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportReqDto {

    Long boardId;

    String contents;

    public ReportReqDto(Long boardId, String contents){
        this.boardId = boardId;
        this.contents = contents;
    }

}
