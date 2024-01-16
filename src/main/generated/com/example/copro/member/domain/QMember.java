package com.example.copro.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1807237522L;

    public static final QMember member = new QMember("member1");

    public final ListPath<com.example.copro.board.domain.Board, com.example.copro.board.domain.QBoard> boards = this.<com.example.copro.board.domain.Board, com.example.copro.board.domain.QBoard>createList("boards", com.example.copro.board.domain.Board.class, com.example.copro.board.domain.QBoard.class, PathInits.DIRECT2);

    public final StringPath career = createString("career");

    public final StringPath email = createString("email");

    public final StringPath gitHubUrl = createString("gitHubUrl");

    public final StringPath language = createString("language");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final ListPath<MemberLike, QMemberLike> memberLikes = this.<MemberLike, QMemberLike>createList("memberLikes", MemberLike.class, QMemberLike.class, PathInits.DIRECT2);

    public final StringPath memberName = createString("memberName");

    public final ListPath<MemberScrapBoard, QMemberScrapBoard> memberScrapBoard = this.<MemberScrapBoard, QMemberScrapBoard>createList("memberScrapBoard", MemberScrapBoard.class, QMemberScrapBoard.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath nickName = createString("nickName");

    public final StringPath occupation = createString("occupation");

    public final StringPath picture = createString("picture");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

