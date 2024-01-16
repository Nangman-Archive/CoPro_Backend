package com.example.copro.comment.api;

import com.example.copro.comment.api.dto.request.CommentReqDto;
import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.comment.application.CommentService;
import com.example.copro.comment.domain.Comment;
import com.example.copro.global.template.RspTemplate;
import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록", description = "댓글 등록 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PostMapping("/{boardId}")
    public RspTemplate<CommentResDto> insert(@PathVariable Long boardId,
                                             @RequestBody CommentReqDto commentReqDto, @AuthenticationPrincipal Member member) {

        CommentResDto commentResDto = commentService.insert(boardId, commentReqDto, member);
        return new RspTemplate<>(HttpStatus.OK
                ,  "댓글 작성 완료"
                , commentResDto
        );
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PutMapping("/{commentId}")
    public RspTemplate<CommentResDto> update(@PathVariable Long commentId, @RequestBody CommentReqDto commentReqDto, @AuthenticationPrincipal Member member) {

        CommentResDto commentResDto = commentService.update(commentId, commentReqDto, member);

        return new RspTemplate<>(HttpStatus.OK
                ,   "댓글 수정 완료"
                ,   commentResDto);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @DeleteMapping("/{commentId}")
    public RspTemplate<Comment> delete(@PathVariable Long commentId) {
        commentService.delete(commentId);

        return new RspTemplate<>(HttpStatus.OK
        ,   "댓글 삭제 완료");
    }

}
