package com.example.copro.board.domain.repository;

import com.example.copro.board.api.dto.response.BoardDto;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.QBoard;
import com.example.copro.comment.domain.QComment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardDto> findAllWithCommentCount(String category, Pageable pageable) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        List<BoardDto> results = queryFactory
                .select(board, comment.count())
                .from(board)
                .leftJoin(comment).on(comment.board.boardId.eq(board.boardId))
                .where(board.category.eq(Category.valueOf(category)))
                .groupBy(board.boardId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> BoardDto.from(tuple.get(board), Optional.ofNullable(tuple.get(comment.count())).orElse(0L).intValue()))
                .collect(Collectors.toList());

        long total = queryFactory
                .selectFrom(board)
                .where(board.category.eq(Category.valueOf(category)))
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<BoardDto> findByTitleContaining(String query, Pageable pageable) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;

        List<BoardDto> results = queryFactory
                .select(board, comment.count())
                .from(board)
                .leftJoin(comment).on(comment.board.boardId.eq(board.boardId))
                .where(board.title.contains(query))
                .groupBy(board.boardId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> BoardDto.from(tuple.get(board), Optional.ofNullable(tuple.get(comment.count())).orElse(0L).intValue()))
                .collect(Collectors.toList());

        long total = queryFactory
                .selectFrom(board)
                .where(board.title.contains(query))
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

}
