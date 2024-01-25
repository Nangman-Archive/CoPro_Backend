package com.example.copro.comment.api.dto.response;

import com.example.copro.comment.domain.Comment;
import com.example.copro.member.api.dto.response.MemberCommentResDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResDto {
    private Long parentId;
    private Long commentId;
    private String content;
    private LocalDateTime createAt;
    private MemberCommentResDto writer;

    private List<CommentResDto> children = new ArrayList<>();


    public CommentResDto(Long parentId, Long commentId, String content,  LocalDateTime createAt, MemberCommentResDto writer) {
        this.parentId = parentId;
        this.commentId = commentId;
        this.content = content;
        this.createAt = createAt;
        this.writer = writer;
    }

    public static CommentResDto from(Comment comment) {
        Long parentId = comment.getParent() != null ? comment.getParent().getCommentId() : -1L;
        return comment.getIsDeleted() ?
                new CommentResDto(parentId,comment.getCommentId(), "삭제된 댓글입니다.", comment.getCreateAt(), null) :
                new CommentResDto(parentId,comment.getCommentId(), comment.getContent(), comment.getCreateAt(),
                        new MemberCommentResDto(comment.getWriter().getNickName(),
                                comment.getWriter().getOccupation()));
    }

}