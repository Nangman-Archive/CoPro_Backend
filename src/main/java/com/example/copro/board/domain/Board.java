package com.example.copro.board.domain;

import com.example.copro.board.api.dto.request.BoardSaveReqDto;
import com.example.copro.image.domain.Image;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberScrapBoard;
import com.example.copro.report.domain.Report;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    @Schema(description = "게시판 id", example = "1")
    private Long boardId;

    @Column(nullable = false)
    @Schema(description = "게시글 제목", example = "제목")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "게시판 종류", example = "공지사항, 자유, 프로젝트")
    private Category category;

    @Column
    @Schema(description = "게시글 내용", example = "내용")
    private String contents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Schema(description = "모집 역할(양식)", example = "프론트엔드, 백엔드, 모바일, AI")
    private Part part ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Schema(description = "목적(양식)", example = "수익창출, 포트폴리오, 기타")
    private Tag tag;

    @Column(columnDefinition = "int default 0")
    @Schema(description = "조회수", example = "11")
    private int count;

    @Column(columnDefinition = "int default 0")
    @Schema(description = "좋아요", example = "2")
    private int heart;

    @Column(columnDefinition = "int default 0")
    private int previousHeartCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MemberHeartBoard> memberHeartBoards = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MemberScrapBoard> memberScrapBoards = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @Schema(description = "이미지, 없으면 0을 요청")
    private List<Image> images = new ArrayList<>(5);

    public void setPreviousHeartCount(int previousHeartCount) {
        this.previousHeartCount = previousHeartCount;
    }

    @Builder
    private Board(String title, Category category, String contents, Part part, Tag tag, int count, int heart,
                  List<Image> images, Member member) {
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.part = part;
        this.tag = tag;
        this.count = count;
        this.images = images;
        this.heart = heart;
        this.member = member;
    }

    public void update(BoardSaveReqDto boardSaveReqDto, List<Image> images) {
        this.title = boardSaveReqDto.title();
        this.category = boardSaveReqDto.category();
        this.part = boardSaveReqDto.part();
        this.contents = boardSaveReqDto.contents();
        this.tag = boardSaveReqDto.tag();
        this.images.addAll(images);
    }

    public void updateViewCount() {
        this.count++;
    }

    public void updateHeartCount() {
        this.heart++;
    }

    public void updateCancelHeartCount() {
        if (this.heart <= 0) {
            this.heart = 0;
        } else {
            this.heart--;
        }
    }

}
