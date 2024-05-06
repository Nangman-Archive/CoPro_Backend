package com.example.copro.board.domain.repository;

import com.example.copro.board.api.dto.response.BoardDto;
import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.QBoard;
import com.example.copro.comment.domain.QComment;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.QBlockedMemberMapping;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardDto> findAllWithCommentCount(Category category, Pageable pageable, Member member) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;
        QBlockedMemberMapping blockedMemberMapping = QBlockedMemberMapping.blockedMemberMapping;

        JPQLQuery<Long> blockedMembersSubQuery = JPAExpressions
                .select(blockedMemberMapping.blockedMember.memberId)
                .from(blockedMemberMapping)
                .where(blockedMemberMapping.member.memberId.eq(member.getMemberId()));

        List<OrderSpecifier<?>> orders = new ArrayList<>();

        pageable.getSort();
        if (pageable.getSort().getOrderFor("count") != null) {
            orders.add(new OrderSpecifier<>(Order.DESC, board.count));
            orders.add(new OrderSpecifier<>(Order.DESC, board.createAt));
        } else if (pageable.getSort().getOrderFor("createAt") != null) {
            orders.add(new OrderSpecifier<>(Order.DESC, board.createAt));
        }

        List<BoardDto> results = queryFactory
                .select(board, comment.count())
                .from(board)
                .leftJoin(comment).on(comment.board.boardId.eq(board.boardId))
                .where(board.category.eq(Category.valueOf(String.valueOf(category)))
                        .and(board.member.memberId.notIn(blockedMembersSubQuery)))
                .groupBy(board.boardId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .fetch()
                .stream()
                .map(tuple -> BoardDto.from(tuple.get(board), Optional.ofNullable(tuple.get(comment.count())).orElse(0L).intValue()))
                .collect(Collectors.toList());

        long total = queryFactory
                .selectFrom(board)
                .where(board.category.eq(Category.valueOf(String.valueOf(category)))
                        .and(board.member.memberId.notIn(blockedMembersSubQuery)))
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<BoardDto> findByTitleContaining(String query, Pageable pageable, Member member) {
        QBoard board = QBoard.board;
        QComment comment = QComment.comment;
        QBlockedMemberMapping blockedMemberMapping = QBlockedMemberMapping.blockedMemberMapping;

        JPQLQuery<Long> blockedMembersSubQuery = JPAExpressions
                .select(blockedMemberMapping.blockedMember.memberId)
                .from(blockedMemberMapping)
                .where(blockedMemberMapping.member.memberId.eq(member.getMemberId()));

        List<BoardDto> results = queryFactory
                .select(board, comment.count())
                .from(board)
                .leftJoin(comment).on(comment.board.boardId.eq(board.boardId))
                .where(board.title.contains(query).and(board.member.memberId.notIn(blockedMembersSubQuery)))
                .groupBy(board.boardId)
                .orderBy(board.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> BoardDto.from(tuple.get(board), Optional.ofNullable(tuple.get(comment.count())).orElse(0L).intValue()))
                .collect(Collectors.toList());

        long total = queryFactory
                .selectFrom(board)
                .where(board.title.contains(query).and(board.member.memberId.notIn(blockedMembersSubQuery)))
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

}
