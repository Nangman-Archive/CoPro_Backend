package com.example.copro.comment.application;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.repository.BoardRepository;
import com.example.copro.comment.api.dto.request.CommentReqDto;
import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.comment.domain.Comment;
import com.example.copro.comment.domain.repository.CommentRepository;
import com.example.copro.member.api.dto.response.MemberCommentResDto;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentResDto insert(Long boardId, CommentReqDto commentReqDto, Member member) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Could not found board id : " + boardId));

        Comment parentComment = null;
        if (commentReqDto.getParentId() != null) {
            parentComment = commentRepository.findById(commentReqDto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Could not found comment id : " + commentReqDto.getParentId()));
        }

        Comment comment = Comment.builder()
                .content(commentReqDto.getContent())
                .writer(member)
                .board(board)
                .parent(parentComment)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return new CommentResDto(savedComment.getCommentId(), savedComment.getContent(), new MemberCommentResDto(savedComment.getWriter()));
    }

    @Transactional
    public CommentResDto update(Long commentId, CommentReqDto commentReqDto, Member member) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Could not found comment id : " + commentId));

        if (!comment.getWriter().getMemberId().equals(member.getMemberId())) {
            throw new NotFoundException("You do not have permission to edit this comment." + member.getMemberId());
        }
        comment.updateContent(commentReqDto.getContent());
        return new CommentResDto(comment.getCommentId(), comment.getContent(), new MemberCommentResDto(comment.getWriter()));
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.findCommentByCommentIdWithParent(commentId)
                .orElseThrow(() -> new NotFoundException("Could not found comment id : " + commentId));
        if(comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
            comment.changeIsDeleted(true);
        } else { // 삭제 가능한 조상 댓글을 구해서 삭제
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    private Comment getDeletableAncestorComment(Comment comment) {
        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if(parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted())
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            return getDeletableAncestorComment(parent);
        return comment; // 삭제해야하는 댓글 반환
    }

}