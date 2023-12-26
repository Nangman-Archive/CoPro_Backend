package com.example.copro.board.domain;

import com.example.copro.board.api.dto.request.BoardSaveReqDto;
import com.example.copro.image.domain.Image;
import com.example.copro.member.domain.Member;
import com.example.copro.member.domain.MemberScrapBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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
    @Schema(description = "게시판 종류", example = "공지사항")
    private Category category;

    @Column(nullable = false)
    @Schema(description = "게시글 내용", example = "내용")
    private String contents;

    @Column
    @Schema(description = "태그", example = "팀빌딩")
    private String tag;

    @Column
    @Schema(description = "조회수", example = "11")
    private int count;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MemberScrapBoard> memberScrapBoard = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true ,cascade = CascadeType.ALL)
    @Schema(description = "이미지, 없으면 0을 요청")
    private List<Image> images = new ArrayList<>(5);

    @Builder
    private Board(String title, Category category, String contents, String tag, int count, List<Image> images) {
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
        this.images = images;
    }

    public void update(BoardSaveReqDto boardSaveReqDto, List<Image> images) {
        this.title = boardSaveReqDto.getTitle();
        this.category = boardSaveReqDto.getCategory();
        this.contents = boardSaveReqDto.getContents();
        this.tag = boardSaveReqDto.getTag();
        this.images.addAll(images);
    }

    public Board updateViewCount(int count) {
        this.count = count + 1;
        return this;
    }

    public void addImage(Image image) {
        this.images.add(image);
    }

}
