package com.example.copro.admin.api;

import com.example.copro.admin.api.dto.request.NoticeSaveReqDto;
import com.example.copro.admin.api.dto.response.ReportInfoResDto;
import com.example.copro.admin.application.AdminService;
import com.example.copro.board.api.dto.response.BoardResDto;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.api.dto.response.MemberResDto;
import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "전체 멤버 정보", description = "전체 멤버 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/infos")
    public RspTemplate<Page<MemberResDto>> membersInfo(@AuthenticationPrincipal Member member,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<MemberResDto> memberInfoResDto = adminService.membersInfo(member, page, size);

        return new RspTemplate<>(HttpStatus.OK, "전체 멤버 조회 완료", memberInfoResDto);
    }

    @Operation(summary = "공지사항 등록", description = "공지사항을 등록 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공", content = @Content(schema = @Schema(implementation = BoardResDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/board")
    public RspTemplate<String> createNotice(@AuthenticationPrincipal Member member,
                                            @Valid @RequestBody NoticeSaveReqDto noticeSaveReqDto) {
        adminService.createNotice(member, noticeSaveReqDto);

        return new RspTemplate<>(HttpStatus.OK, "공지사항 등록 성공", "공지사항 등록");
    }

    @Operation(summary = "공지사항 수정", description = "공지사항을 수정 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BoardResDto.class))),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PutMapping("/board")
    public RspTemplate<String> updateNotice(@AuthenticationPrincipal Member member,
                                            @RequestParam("boardId") Long boardId,
                                            @Valid @RequestBody NoticeSaveReqDto noticeSaveReqDto) {
        adminService.updateNotice(member, boardId, noticeSaveReqDto);

        return new RspTemplate<>(HttpStatus.OK, "공지사항 수정 성공", "공지사항 수정");
    }

    @Operation(summary = "게시물 삭제", description = "게시물 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @DeleteMapping("/board")
    public RspTemplate<String> deleteBoard(@AuthenticationPrincipal Member member,
                                           @RequestParam("boardId") Long boardId) {
        adminService.deleteBoard(member, boardId);

        return new RspTemplate<>(HttpStatus.OK, "게시물 삭제 성공", "게시물 삭제");
    }

    @Operation(summary = "신고 내역 조회", description = "신고 내역을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/report")
    public RspTemplate<Page<ReportInfoResDto>> reportsInfo(@AuthenticationPrincipal Member member,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ReportInfoResDto> reports = adminService.reports(member, page, size);

        return new RspTemplate<>(HttpStatus.OK, "신고 내역 조회 성공", reports);
    }

}
