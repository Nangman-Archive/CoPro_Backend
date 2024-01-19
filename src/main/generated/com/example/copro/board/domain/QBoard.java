package com.example.copro.board.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 1173224002L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final StringPath contents = createString("contents");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Integer> heart = createNumber("heart", Integer.class);

    public final ListPath<com.example.copro.image.domain.Image, com.example.copro.image.domain.QImage> images = this.<com.example.copro.image.domain.Image, com.example.copro.image.domain.QImage>createList("images", com.example.copro.image.domain.Image.class, com.example.copro.image.domain.QImage.class, PathInits.DIRECT2);

    public final com.example.copro.member.domain.QMember member;

    public final ListPath<MemberHeartBoard, QMemberHeartBoard> memberHeartBoards = this.<MemberHeartBoard, QMemberHeartBoard>createList("memberHeartBoards", MemberHeartBoard.class, QMemberHeartBoard.class, PathInits.DIRECT2);

    public final ListPath<com.example.copro.member.domain.MemberScrapBoard, com.example.copro.member.domain.QMemberScrapBoard> memberScrapBoards = this.<com.example.copro.member.domain.MemberScrapBoard, com.example.copro.member.domain.QMemberScrapBoard>createList("memberScrapBoards", com.example.copro.member.domain.MemberScrapBoard.class, com.example.copro.member.domain.QMemberScrapBoard.class, PathInits.DIRECT2);

    public final NumberPath<Integer> previousHeartCount = createNumber("previousHeartCount", Integer.class);

    public final ListPath<com.example.copro.report.domain.Report, com.example.copro.report.domain.QReport> reports = this.<com.example.copro.report.domain.Report, com.example.copro.report.domain.QReport>createList("reports", com.example.copro.report.domain.Report.class, com.example.copro.report.domain.QReport.class, PathInits.DIRECT2);

    public final StringPath tag = createString("tag");

    public final StringPath title = createString("title");

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.copro.member.domain.QMember(forProperty("member")) : null;
    }

}

