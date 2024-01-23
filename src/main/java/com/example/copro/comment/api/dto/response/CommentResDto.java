package com.example.copro.comment.api.dto.response;

import com.example.copro.comment.domain.Comment;
import com.example.copro.member.api.dto.response.MemberCommentResDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResDto {
    private Long commentId;
    private String content;
    private MemberCommentResDto writer;
    private List<CommentResDto> children = new ArrayList<>();

    public CommentResDto(Long commentId, String content, MemberCommentResDto writer) {
        this.commentId = commentId;
        this.content = content;
        this.writer = writer;
    }

    public static CommentResDto from(Comment comment) {
        return comment.getIsDeleted() ?
                new CommentResDto(comment.getCommentId(), "삭제된 댓글입니다.", null) :
                new CommentResDto(comment.getCommentId(), comment.getContent(), new MemberCommentResDto(comment.getWriter().getNickName(), comment.getWriter().getOccupation()));
    }

}