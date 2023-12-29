package com.example.copro.board.domain;

import com.example.copro.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberHeartBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "좋아요 id", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    private MemberHeartBoard(Long id, Board board, Member member){
        this.id = id;
        this.board = board;
        this.member = member;
    }

}
