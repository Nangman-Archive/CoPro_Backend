package com.example.copro.comment.domain.repository;

import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.comment.domain.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentCustomRepository {

    List<CommentResDto> findByBoardBoardId(Long boardId);

    Optional<Comment> findCommentByCommentIdWithParent(Long id);
}
