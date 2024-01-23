package com.example.copro.comment.domain;

import com.example.copro.board.domain.BaseTimeEntity;
import com.example.copro.board.domain.Board;
import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert//Hibernate의 어노테이션으로, insert SQL을 실행할 때 null인 필드를 제외합니다. 이를 통해 SQL을 최적화할 수 있습니다.
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @Schema(description = "댓글 id", example = "1")
    private Long commentId;

    @Column(nullable = false, length = 1000)
    @Schema(description = "댓글 내용", example = "댓글입니다")
    private String content;

    @Column(columnDefinition = "boolean default false")
    @Schema(description = "삭제 유무", example = "false")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @Schema(description = "부모 댓글 id", example = "1")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(String content, Boolean isDeleted, Member writer, Board board, Comment parent) {
        this.content = content;
        this.isDeleted = isDeleted;
        this.writer = writer;
        this.board = board;
        this.parent = parent;
    }

    public void changeIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}

