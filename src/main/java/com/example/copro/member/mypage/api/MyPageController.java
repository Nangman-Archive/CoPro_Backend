package com.example.copro.member.mypage.api;

import com.example.copro.board.api.dto.response.BoardListRspDto;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.api.dto.request.UpdateViewTypeReqDto;
import com.example.copro.member.api.dto.response.MemberLikeResDto;
import com.example.copro.member.mypage.api.dto.response.MyProfileInfoResDto;
import com.example.copro.member.mypage.application.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "myPage", description = "MyPage Controller")
@RestController
@RequestMapping("/api")
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @Operation(summary = "내 프로필 정보", description = "내 프로필을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 프로필 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/{memberId}/profile")
    public RspTemplate<MyProfileInfoResDto> myProfileInfo(@PathVariable(name = "memberId") Long memberId) {
        MyProfileInfoResDto memberResDto = myPageService.myProfileInfo(memberId);
        return new RspTemplate<>(HttpStatus.OK, "내 프로필 정보", memberResDto);
    }

    @Operation(summary = "내 관심 프로필 목록 ", description = "본인이 좋아요한 유저의 목록을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요한 유저 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/{memberId}/likes")
    public RspTemplate<Page<MemberLikeResDto>> memberLikeList(@PathVariable(name = "memberId") Long memberId,
                                                              @RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<MemberLikeResDto> memberLikeResDtos = myPageService.memberLikeList(memberId, page, size);
        return new RspTemplate<>(HttpStatus.OK, "내 관심 프로필 목록", memberLikeResDtos);
    }

    @Operation(summary = "내 관심 게시물 목록", description = "내 관심 게시물 목록을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 관심 게시물 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/{memberId}/scrap")
    public RspTemplate<BoardListRspDto> myScrapBoard(@PathVariable(name = "memberId") Long memberId,
                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        BoardListRspDto boardListRspDto = myPageService.boardLikeList(memberId, page, size);
        return new RspTemplate<>(HttpStatus.OK, "내 관심 게시물 목록", boardListRspDto);
    }

    @Operation(summary = "내가 작성한 게시물 목록", description = "내가 작성한 게시물 목록을 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 작성한 게시물 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/{memberId}/write")
    public RspTemplate<BoardListRspDto> myWriteBoard(@PathVariable(name = "memberId") Long memberId,
                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        BoardListRspDto boardListRspDto = myPageService.boardWriteList(memberId, page, size);
        return new RspTemplate<>(HttpStatus.OK, "내가 작성한 게시물 목록", boardListRspDto);
    }

    @Operation(summary = "나의 뷰 타입 변경", description = "나의 뷰 타입을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "뷰 타입 변경 성공"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping("/{memberId}/view-type")
    public RspTemplate<Integer> UpdateViewType(@PathVariable(name = "memberId") Long memberId,
                                               @RequestBody UpdateViewTypeReqDto updateViewTypeReqDto) {
        myPageService.updateViewType(memberId, updateViewTypeReqDto);
        return new RspTemplate<>(HttpStatus.OK, "뷰 타입 변경", updateViewTypeReqDto.viewType());
    }
}
