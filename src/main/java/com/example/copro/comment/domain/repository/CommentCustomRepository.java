package com.example.copro.comment.domain.repository;

import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.comment.domain.Comment;
import com.example.copro.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentCustomRepository {
    Page<CommentResDto> findByBoardBoardId(Long boardId, Pageable pageable, Member member);

    Optional<Comment> findCommentByCommentIdWithParent(Long id);
}
