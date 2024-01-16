package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.comment.api.dto.response.CommentResDto;
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

    private int heart;

    private List<String> imageUrl;

    private Long memberId;

    private String occupation;

    private List<Long> heartMemberIds;
    private List<Long> scrapMemberIds;

    private List<CommentResDto> commentResDtoList;

    @Builder
    public BoardResDto(Long boardId, String title, Category category, String contents, String tag, int count, Long memberId, String occupation, int heart, List<String> imageUrl, List<Long> heartMemberIds, List<Long> scrapMemberIds,List<CommentResDto> commentResDtoList) {
        this.boardId = boardId;
        this.title = title;
        this.category = category;
        this.contents = contents;
        this.tag = tag;
        this.count = count;
        this.memberId = memberId;
        this.occupation = occupation;
        this.heart = heart;
        this.imageUrl = imageUrl;
        this.heartMemberIds = heartMemberIds;
        this.scrapMemberIds = scrapMemberIds;
        this.commentResDtoList = commentResDtoList;
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
                .memberId(board.getMember().getMemberId())
                .occupation(board.getMember().getOccupation())
                .heart(board.getHeart())
                .imageUrl(imageUrl)
                .build();
    }

    public static BoardResDto from(Board board, List<Long> heartMemberIds, List<Long> scrapMemberIds, List<CommentResDto> commentResDtoList) {
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
                .memberId(board.getMember().getMemberId())
                .occupation(board.getMember().getOccupation())
                .heart(board.getHeart())
                .imageUrl(imageUrl)
                .heartMemberIds(heartMemberIds)
                .scrapMemberIds(scrapMemberIds)
                .commentResDtoList(commentResDtoList)
                .build();
    }

}
