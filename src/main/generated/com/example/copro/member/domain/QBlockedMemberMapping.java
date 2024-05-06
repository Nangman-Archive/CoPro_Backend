package com.example.copro.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlockedMemberMapping is a Querydsl query type for BlockedMemberMapping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlockedMemberMapping extends EntityPathBase<BlockedMemberMapping> {

    private static final long serialVersionUID = 16622364L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlockedMemberMapping blockedMemberMapping = new QBlockedMemberMapping("blockedMemberMapping");

    public final QMember blockedMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public QBlockedMemberMapping(String variable) {
        this(BlockedMemberMapping.class, forVariable(variable), INITS);
    }

    public QBlockedMemberMapping(Path<? extends BlockedMemberMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlockedMemberMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlockedMemberMapping(PathMetadata metadata, PathInits inits) {
        this(BlockedMemberMapping.class, metadata, inits);
    }

    public QBlockedMemberMapping(Class<? extends BlockedMemberMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.blockedMember = inits.isInitialized("blockedMember") ? new QMember(forProperty("blockedMember")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

