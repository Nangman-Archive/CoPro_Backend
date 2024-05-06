package com.example.copro.member.api;

import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.api.dto.request.BlockedMemberReqDto;
import com.example.copro.member.application.BlockedMemberService;
import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "blockedMember", description = "BlockedMember Controller")
@RestController
@RequestMapping("/api")
public class BlockedMemberController {
    private final BlockedMemberService blockedMemberService;

    public BlockedMemberController(BlockedMemberService blockedMemberService) {
        this.blockedMemberService = blockedMemberService;
    }

    @Operation(summary = "유저 차단", description = "해당 유저를 차단 목록에 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 차단 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping("/add-blocked")
    public RspTemplate<String> addBlockedMember(@AuthenticationPrincipal Member member,
                                                @Valid @RequestBody BlockedMemberReqDto blockedMemberReqDto) {
        blockedMemberService.addBlockedMember(member, blockedMemberReqDto);
        return new RspTemplate<>(HttpStatus.OK, "유저 차단 완료");
    }

    @Operation(summary = "유저 차단 취소", description = "해당 유저를 차단 목록에서 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 차단 취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping("/cancel-blocked")
    public RspTemplate<String> cancelBlockedMember(@AuthenticationPrincipal Member member,
                                                   @Valid @RequestBody BlockedMemberReqDto blockedMemberReqDto) {
        blockedMemberService.cancelBlockedMember(member, blockedMemberReqDto);
        return new RspTemplate<>(HttpStatus.OK, "유저 차단 취소 완료");
    }
}
