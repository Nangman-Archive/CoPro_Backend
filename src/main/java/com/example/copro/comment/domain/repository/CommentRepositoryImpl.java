package com.example.copro.comment.domain.repository;

import static com.example.copro.comment.api.dto.response.CommentResDto.from;
import static com.example.copro.comment.domain.QComment.comment;

import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.comment.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentResDto> findByBoardBoardId(Long boardId, Pageable pageable) {
        long total = queryFactory.selectFrom(comment)
                .where(comment.board.boardId.eq(boardId), comment.parent.isNull())
                .fetchCount();

        // QueryDSL을 사용해서 comment 테이블에서 데이터를 조회
        // 특정 게시판(boardId)의 댓글만 가져오며, 부모 댓글과 함께 가져옴
        // 댓글의 부모 ID를 오름차순으로 정렬하고, 같은 부모 ID를 가진 댓글 중에서는 생성 시간 기준으로 오름차순 정렬
        List<Comment> parentComments = queryFactory.selectFrom(comment)
                .where(comment.board.boardId.eq(boardId), comment.parent.isNull())
                .orderBy(comment.createAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<CommentResDto> commentResDtoList = new ArrayList<>();

        for (Comment parentComment : parentComments) {
            CommentResDto parentCommentResDto = from(parentComment);
            commentResDtoList.add(parentCommentResDto);

            List<Comment> childComments = queryFactory.selectFrom(comment)
                    .where(comment.parent.eq(parentComment))
                    .orderBy(comment.createAt.asc())
                    .fetch();

            for (Comment childComment : childComments) {
                CommentResDto childCommentResDto = from(childComment);
                parentCommentResDto.getChildren().add(childCommentResDto);
            }
        }

        return new PageImpl<>(commentResDtoList, pageable, total);
    }

    @Override
    public Optional<Comment> findCommentByCommentIdWithParent(Long commentId) {

        Comment selectedComment = queryFactory.selectFrom(comment) // comment 테이블에서 데이터를 선택
                .leftJoin(comment.parent).fetchJoin() // 부모 댓글 데이터를 함께 가져오도록 조인
                .where(comment.commentId.eq(commentId)) // commentId가 입력된 commentId와 동일한 comment 선택
                .fetchOne(); // 쿼리의 결과를 하나 가져온다. 결과가 없거나 두 개 이상일 경우, 예외 발생.

        // 선택된 댓글을 Optional로 감싸 반환. 만약 선택된 댓글이 없으면 Optional.empty()가 반환.
        return Optional.ofNullable(selectedComment);
    }
}
