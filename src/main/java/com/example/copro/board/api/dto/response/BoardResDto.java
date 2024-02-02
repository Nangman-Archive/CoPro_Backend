package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.Part;
import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.image.domain.Image;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record BoardResDto(
        Long boardId,
        String title,
        LocalDateTime createAt,
        Category category,
        String contents,
        String summary,
        Part part,
        String tag,
        int count,
        int heart,
        List<String> imageUrl,
        String nickName,
        String occupation,
        boolean isHeart,
        boolean isScrap,
        int commentCount
) {
    public static BoardResDto of(Board board) {
        List<String> imageUrl = board.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        return BoardResDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .createAt(board.getCreateAt())
                .category(board.getCategory())
                .contents(board.getContents())
                .summary(board.getSummary())
                .part(board.getPart())
                .tag(board.getTag())
                .count(board.getCount())
                .nickName(board.getMember().getNickName())
                .occupation(board.getMember().getOccupation())
                .heart(board.getHeart())
                .imageUrl(imageUrl)
                .build();
    }

    public static BoardResDto from(Board board, boolean isHeart, boolean isScrap, int commentCount) {
        List<String> imageUrl = board.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        return BoardResDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .createAt(board.getCreateAt())
                .category(board.getCategory())
                .contents(board.getContents())
                .summary(board.getSummary())
                .part(board.getPart())
                .tag(board.getTag())
                .count(board.getCount())
                .nickName(board.getMember().getNickName())
                .occupation(board.getMember().getOccupation())
                .heart(board.getHeart())
                .imageUrl(imageUrl)
                .isHeart(isHeart)
                .isScrap(isScrap)
                .commentCount(commentCount)
                .build();
    }

}
