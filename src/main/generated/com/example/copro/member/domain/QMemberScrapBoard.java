package com.example.copro.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberScrapBoard is a Querydsl query type for MemberScrapBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberScrapBoard extends EntityPathBase<MemberScrapBoard> {

    private static final long serialVersionUID = 2033506979L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberScrapBoard memberScrapBoard = new QMemberScrapBoard("memberScrapBoard");

    public final com.example.copro.board.domain.QBoard board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public QMemberScrapBoard(String variable) {
        this(MemberScrapBoard.class, forVariable(variable), INITS);
    }

    public QMemberScrapBoard(Path<? extends MemberScrapBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberScrapBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberScrapBoard(PathMetadata metadata, PathInits inits) {
        this(MemberScrapBoard.class, metadata, inits);
    }

    public QMemberScrapBoard(Class<? extends MemberScrapBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.example.copro.board.domain.QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

