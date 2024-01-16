package com.example.copro.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberHeartBoard is a Querydsl query type for MemberHeartBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberHeartBoard extends EntityPathBase<MemberHeartBoard> {

    private static final long serialVersionUID = 1173830558L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberHeartBoard memberHeartBoard = new QMemberHeartBoard("memberHeartBoard");

    public final QBoard board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.copro.member.domain.QMember member;

    public QMemberHeartBoard(String variable) {
        this(MemberHeartBoard.class, forVariable(variable), INITS);
    }

    public QMemberHeartBoard(Path<? extends MemberHeartBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberHeartBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberHeartBoard(PathMetadata metadata, PathInits inits) {
        this(MemberHeartBoard.class, metadata, inits);
    }

    public QMemberHeartBoard(Class<? extends MemberHeartBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.member = inits.isInitialized("member") ? new com.example.copro.member.domain.QMember(forProperty("member")) : null;
    }

}

