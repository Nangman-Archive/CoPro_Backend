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
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResDto> findByBoardBoardId(Long boardId) {
        // QueryDSL을 사용해서 comment 테이블에서 데이터를 조회
        // 특정 게시판(boardId)의 댓글만 가져오며, 부모 댓글과 함께 가져옴
        // 댓글의 부모 ID를 오름차순으로 정렬하고, 같은 부모 ID를 가진 댓글 중에서는 생성 시간 기준으로 오름차순 정렬
        List<Comment> comments = queryFactory.selectFrom(comment) // comment 테이블에서 데이터를 선택
                .leftJoin(comment.parent).fetchJoin() // 부모 댓글 데이터를 함께 가져오도록 조인
                .where(comment.board.boardId.eq(boardId)) // 게시판 ID가 boardId와 같은 댓글만 선택
                .orderBy(comment.parent.commentId.asc().nullsFirst(), // 부모 댓글 ID를 오름차순으로 정렬, 부모 댓글 ID가 없는 댓글은 먼저 표시
                        comment.createAt.asc()) // 그 다음은 생성 시간을 기준으로 오름차순 정렬
                .fetch(); // 쿼리 실행 및 결과 가져오기

        // 변환된 CommentResDto 객체를 저장할 리스트 초기화
        List<CommentResDto> commentResDtoList = new ArrayList<>();

        // 댓글의 ID를 키로, CommentResDto 객체를 값으로 가지는 맵 초기화
        // 빠른 댓글 -> CommentResDto 매칭을 위해 사용됨
        Map<Long, CommentResDto> commentDtoHashMap = new HashMap<>();

        // 조회된 댓글들을 순회하며 CommentResDto 객체로 변환하고, 이를 적절한 위치에 추가
        comments.forEach(c -> {
            // 댓글을 CommentResDto 객체로 변환
            CommentResDto commentResDto = from(c);

            // 변환된 CommentResDto 객체를 맵에 추가
            commentDtoHashMap.put(commentResDto.commentId(), commentResDto);

            // 댓글이 부모 댓글을 가지면 부모 댓글의 CommentResDto 객체의 자식 리스트에 추가
            if (c.getParent() != null) commentDtoHashMap.get(c.getParent().getCommentId()).children().add(commentResDto);
                // 댓글이 부모 댓글을 가지지 않으면 commentResDtoList에 직접 추가
            else commentResDtoList.add(commentResDto);
        });

        // 변환된 CommentResDto 객체들을 반환
        return commentResDtoList;
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

    @Override
    public int countByBoardBoardId(Long boardId) {

        return (int)queryFactory.selectFrom(comment)
                .where(comment.board.boardId.eq(boardId))
                .fetchCount();
    }

}
