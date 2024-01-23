package com.example.copro.comment.api.dto.response;

import com.example.copro.comment.domain.Comment;
import com.example.copro.member.api.dto.response.MemberCommentResDto;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record CommentResDto(
        Long commentId,
        String content,
        MemberCommentResDto writer,
        List<CommentResDto> children
) {
    public static CommentResDto from(Comment comment) {
        if (comment.getIsDeleted()) {
            return new CommentResDto(comment.getCommentId(), "삭제된 댓글입니다.", null, Collections.emptyList());
        } else {
            List<CommentResDto> children = comment.getChildren().stream()
                    .map(CommentResDto::from)
                    .collect(Collectors.toList());

            return new CommentResDto(
                    comment.getCommentId(),
                    comment.getContent(),
                    new MemberCommentResDto(comment.getWriter().getNickName(), comment.getWriter().getOccupation()),
                    children
            );
        }
    }
}
