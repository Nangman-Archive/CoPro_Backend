package com.example.copro.member.api;

import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.api.dto.request.LikeMemberReqDto;
import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.api.dto.respnse.MemberLikeResDto;
import com.example.copro.member.api.dto.respnse.MemberResDto;
import com.example.copro.member.application.MemberService;
import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/success")
    public RspTemplate<String> loginSuccess() {
        return new RspTemplate<>(HttpStatus.OK, "SUCCESS");
    }

    @Operation(summary = "전체 멤버 정보", description = "전체 멤버 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/infos")
    public RspTemplate<Page<MemberResDto>> membersInfo(
            @RequestParam(name = "occupation", required = false) String occupation,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "career", required = false) String career,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<MemberResDto> memberResDto = memberService.memberInfoList(occupation, language, career, page, size);

        return new RspTemplate<>(HttpStatus.OK, "전체 멤버 조회 완료", memberResDto);
    }

    @Operation(summary = "프로필 수정", description = "프로필에 개발직군, 주력언어, 다룬기간, 그리고 깃허브주소를 업데이트 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PatchMapping("/{memberId}")
    public RspTemplate<MemberResDto> memberProfileUpdate(@PathVariable(name = "memberId") Long memberId,
                                                         @RequestBody MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        MemberResDto memberResDto = memberService.profileUpdate(memberId, memberProfileUpdateReqDto);
        return new RspTemplate<>(HttpStatus.OK, "프로필 수정 완료", memberResDto);
    }

    @Operation(summary = "좋아요 유저 목록", description = "본인이 좋아요한 유저의 목록을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요한 유저 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/{memberId}/likes")
    public RspTemplate<Page<MemberLikeResDto>> memberLikeList(@PathVariable(name = "memberId") Long memberId,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<MemberLikeResDto> memberLikeResDtos = memberService.memberLikeList(memberId, page, size);
        return new RspTemplate<>(HttpStatus.OK, "좋아요한 유저 목록", memberLikeResDtos);
    }

    @Operation(summary = "유저 좋아요", description = "해당 유저를 관심유저 목록에 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 좋아요 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PatchMapping("/{memberId}/add-like")
    public RspTemplate<String> addLikeMember(@PathVariable(name = "memberId") Long memberId,
                                             @RequestBody LikeMemberReqDto likeMemberReqDto) {
        memberService.addMemberLike(memberId, likeMemberReqDto);
        return new RspTemplate<>(HttpStatus.OK, "유저 좋아요 추가 완료");
    }

    @Operation(summary = "유저 좋아요 취소", description = "해당 유저를 관심유저 목록에서 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PatchMapping("/{memberId}/cancel-like")
    public RspTemplate<String> cancelLikeMember(@PathVariable(name = "memberId") Long memberId,
                                                @RequestBody LikeMemberReqDto likeMemberReqDto) {
        memberService.cancelMemberLike(memberId, likeMemberReqDto);
        return new RspTemplate<>(HttpStatus.OK, "유저 좋아요 취소 완료");
    }
}
