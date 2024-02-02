package com.example.copro.comment.api;

import com.example.copro.comment.api.dto.request.CommentSaveReqDto;
import com.example.copro.comment.api.dto.request.CommentUpdateReqDto;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public RspTemplate<CommentResDto> insert(@PathVariable(name = "boardId") Long boardId,
                                             @Valid @RequestBody CommentSaveReqDto commentSaveReqDto,
                                             @AuthenticationPrincipal Member member) {

        CommentResDto commentResDto = commentService.insert(boardId, commentSaveReqDto, member);
        return new RspTemplate<>(HttpStatus.OK
                , "댓글 작성 완료"
                , commentResDto
        );
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @PutMapping("/{commentId}")
    public RspTemplate<CommentResDto> update(@PathVariable(name = "commentId") Long commentId,
                                             @Valid @RequestBody CommentUpdateReqDto commentUpdateReqDto,
                                             @AuthenticationPrincipal Member member) {

        CommentResDto commentResDto = commentService.update(commentId, commentUpdateReqDto, member);

        return new RspTemplate<>(HttpStatus.OK
                , "댓글 수정 완료"
                , commentResDto);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제 합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @DeleteMapping("/{commentId}")
    public RspTemplate<Comment> delete(@PathVariable(name = "commentId") Long commentId,
                                       @AuthenticationPrincipal Member member) {
        commentService.delete(member, commentId);

        return new RspTemplate<>(HttpStatus.OK
                , "댓글 삭제 완료");
    }


    @Operation(summary = "상세 페이지 댓글 조회", description = "상세 페이지의 댓글을 조회 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증실패", content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN"))),
    })
    @GetMapping("/{boardId}/comments")
    public RspTemplate<Page<CommentResDto>> getCommentsByBoard(@PathVariable(name = "boardId") Long boardId,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<CommentResDto> commentsByBoard = commentService.getCommentsByBoard(boardId, page, size);
        return new RspTemplate<>(HttpStatus.OK, "상세 페이지 댓글 조회 성공", commentsByBoard);
    }

}
