package com.example.copro.board.domain;

import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberScrapBoard;
import com.example.copro.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String contents;

    @Column
    private String tag;

    @Column
    private int count;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MemberScrapBoard> memberScrapBoard = new ArrayList<>();

    @ManyToOne //단방향
    @JoinColumn(name="member_id",nullable = false)
    private Member member;

    @Builder
    private Board(String title, Category category, String contents, String tag, int count, Member member) {
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
        this.member = member;
    }

    public void update(String title, Category category, String contents, String tag) {
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;

    }

    public Board updateViewCount(int count){
        this.count = count+1;
        return this;
    }

}
