package com.example.copro.member.api;

import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.api.dto.respnse.MemberResDto;
import com.example.copro.member.api.dto.respnse.MembersResDto;
import com.example.copro.member.application.MemberService;
import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "member", description = "Member Controller")
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "로그인 성공", description = "로그인 시에 불러올 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/success")
    public RspTemplate<String> loginSuccess() {
        return new RspTemplate<>(HttpStatus.OK, "SUCCESS");
    }

    @Operation(summary = "전체 멤버 정보", description = "전체 멤버 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/info")
    public RspTemplate<MembersResDto> membersInfo(
            @RequestParam(name = "occupation", required = false) String occupation,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "career", required = false) String career) {

        MembersResDto membersResDto;
        membersResDto = memberService.memberInfoList(occupation, language, career);
//        if (occupation == null && language == null && career == null) {
//            return new RspTemplate<>(HttpStatus.OK, "검색 없는 조회");
//        } else {
//
//        }

        return new RspTemplate<>(HttpStatus.OK, "전체 멤버 조회 완료", membersResDto);
    }

    @Operation(summary = "프로필 수정", description = "프로필에 개발직군, 주력언어, 다룬기간을 업데이트 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/{memberId}")
    public RspTemplate<MemberResDto> memberProfileUpdate(@PathVariable(name = "memberId") Long memberId,
                                                         @RequestBody MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        MemberResDto memberResDto = memberService.profileUpdate(memberId, memberProfileUpdateReqDto);
        return new RspTemplate<>(HttpStatus.OK, "프로필 수정 완료", memberResDto);
    }
}
