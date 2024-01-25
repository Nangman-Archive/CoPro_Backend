package com.example.copro.member.api;

import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.api.dto.request.MemberGitHubUrlUpdateReqDto;
import com.example.copro.member.api.dto.request.MemberLikeReqDto;
import com.example.copro.member.api.dto.request.MemberProfileUpdateReqDto;
import com.example.copro.member.api.dto.response.MemberChattingProfileResDto;
import com.example.copro.member.api.dto.response.MemberInfoResDto;
import com.example.copro.member.api.dto.response.MemberResDto;
import com.example.copro.member.application.MemberService;
import com.example.copro.member.domain.Member;
import com.example.copro.member.exception.ExistsNickNameException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/success")
    public RspTemplate<String> loginSuccess() {
        return new RspTemplate<>(HttpStatus.OK, "SUCCESS");
    }

    @Operation(summary = "채팅 프로필 정보", description = "채팅뷰 멤버 프로필을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅 프로필 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/chatting/profile/{nickName}")
    public RspTemplate<MemberChattingProfileResDto> memberChattingProfileInfo(
            @PathVariable(name = "nickName") String nickName) {
        MemberChattingProfileResDto memberChattingProfileResDto = memberService.memberChattingProProfileInfo(nickName);
        return new RspTemplate<>(HttpStatus.OK, "멤버 채팅 프로필 정보 조회", memberChattingProfileResDto);
    }

    @Operation(summary = "카드뷰 전체 멤버 정보", description = "카드뷰 전체 멤버 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/infos")
    public RspTemplate<MemberInfoResDto> membersInfo(
            @AuthenticationPrincipal Member member,
            @RequestParam(name = "occupation", required = false) String occupation,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "career", required = false) String career,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        MemberInfoResDto memberInfoResDto =
                memberService.memberInfoList(member, occupation, language, career, page, size);

        return new RspTemplate<>(HttpStatus.OK, "전체 멤버 조회 완료", memberInfoResDto);
    }

    @Operation(summary = "프로필 수정", description = "프로필에 개발직군, 주력언어, 다룬기간을 업데이트 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping("/profile")
    public RspTemplate<MemberResDto> memberProfileUpdate(@AuthenticationPrincipal Member member,
                                                         @RequestBody MemberProfileUpdateReqDto memberProfileUpdateReqDto) {
        MemberResDto memberResDto = memberService.profileUpdate(member, memberProfileUpdateReqDto);
        return new RspTemplate<>(HttpStatus.OK, "프로필 수정 완료", memberResDto);
    }

    @GetMapping("/nickname")
    public RspTemplate<Void> duplicateNickName(@RequestParam(name = "nickName") String nickName) {
        try {
            memberService.validateDuplicateNickName(nickName);
            return new RspTemplate<>(HttpStatus.OK, "사용 가능한 닉네임입니다.");
        } catch (ExistsNickNameException e) {
            return new RspTemplate<>(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "깃 허브 주소 수정", description = "프로필에 깃허브주소를 업데이트 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping("/github-url")
    public RspTemplate<MemberResDto> memberGitHubUrlUpdate(@AuthenticationPrincipal Member member,
                                                           @RequestBody MemberGitHubUrlUpdateReqDto memberGitHubUrlUpdateReqDto) {
        MemberResDto memberResDto = memberService.gitHubUrlUpdate(member, memberGitHubUrlUpdateReqDto);
        return new RspTemplate<>(HttpStatus.OK, "깃 허브 주소 수정 완료", memberResDto);
    }

    @Operation(summary = "유저 좋아요", description = "해당 유저를 관심유저 목록에 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 좋아요 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping("/add-like")
    public RspTemplate<String> addLikeMember(@AuthenticationPrincipal Member member,
                                             @RequestBody MemberLikeReqDto memberLikeReqDto) {
        memberService.addMemberLike(member, memberLikeReqDto);
        return new RspTemplate<>(HttpStatus.OK, "유저 좋아요 추가 완료");
    }

    @Operation(summary = "유저 좋아요 취소", description = "해당 유저를 관심유저 목록에서 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping("/cancel-like")
    public RspTemplate<String> cancelLikeMember(@AuthenticationPrincipal Member member,
                                                @RequestBody MemberLikeReqDto memberLikeReqDto) {
        memberService.cancelMemberLike(member, memberLikeReqDto);
        return new RspTemplate<>(HttpStatus.OK, "유저 좋아요 취소 완료");
    }
}
