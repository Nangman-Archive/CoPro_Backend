package com.example.copro.report.api;

import com.example.copro.board.application.BoardService;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.domain.Member;
import com.example.copro.report.api.dto.request.ReportReqDto;
import com.example.copro.report.api.dto.response.ReportResDto;
import com.example.copro.report.application.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
@Tag(name = "report", description = "Report Controller")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "게시글 신고", description = "게시글 신고 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping() //게시글 신고
    public RspTemplate<Void> reportBoard(@RequestBody ReportReqDto reportReqDto,@AuthenticationPrincipal Member member) {
        reportService.reportBoard(reportReqDto,member.getMemberId());
        return new RspTemplate<>(HttpStatus.OK, reportReqDto.getBoardId() + "번 게시물 신고 완료");
    }
}