package com.example.copro.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberLike is a Querydsl query type for MemberLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberLike extends EntityPathBase<MemberLike> {

    private static final long serialVersionUID = -1804928219L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberLike memberLike = new QMemberLike("memberLike");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember likedMember;

    public final QMember member;

    public QMemberLike(String variable) {
        this(MemberLike.class, forVariable(variable), INITS);
    }

    public QMemberLike(Path<? extends MemberLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberLike(PathMetadata metadata, PathInits inits) {
        this(MemberLike.class, metadata, inits);
    }

    public QMemberLike(Class<? extends MemberLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.likedMember = inits.isInitialized("likedMember") ? new QMember(forProperty("likedMember")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

