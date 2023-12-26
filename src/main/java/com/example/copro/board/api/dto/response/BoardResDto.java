package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.image.domain.Image;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResDto {

    private Long boardId;
    private String title;

    private Category category;
    private String contents;

    private String tag;

    private int count;

    private List<String> imageUrl;

    @Builder
    public BoardResDto(Long boardId, String title, Category category, String contents, String tag, int count, List<String> imageUrl) {
        this.boardId = boardId;
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
        this.imageUrl = imageUrl;
    }

    public static BoardResDto of(Board board) {
        List<String> imageUrl = board.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        return BoardResDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .category(board.getCategory())
                .contents(board.getContents())
                .tag(board.getTag())
                .count(board.getCount())
                .imageUrl(imageUrl)
                .build();
    }

}
