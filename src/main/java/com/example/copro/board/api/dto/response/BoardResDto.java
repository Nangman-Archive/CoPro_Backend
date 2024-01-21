package com.example.copro.board.api.dto.response;

import com.example.copro.board.domain.Board;
import com.example.copro.board.domain.Category;
import com.example.copro.comment.api.dto.response.CommentResDto;
import com.example.copro.image.domain.Image;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record BoardResDto(
        Long boardId,
        String title,
        Category category,
        String contents,
        String tag,
        int count,
        int heart,
        List<String> imageUrl,
        String nickName,
        String occupation,
        List<Long> heartMemberIds,
        List<Long> scrapMemberIds,
        List<CommentResDto> commentResDtoList
) {
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
                .nickName(board.getMember().getNickName())
                .occupation(board.getMember().getOccupation())
                .heart(board.getHeart())
                .imageUrl(imageUrl)
                .build();
    }

    public static BoardResDto from(Board board, List<Long> heartMemberIds, List<Long> scrapMemberIds,
                                   List<CommentResDto> commentResDtoList) {
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
                .nickName(board.getMember().getNickName())
                .occupation(board.getMember().getOccupation())
                .heart(board.getHeart())
                .imageUrl(imageUrl)
                .heartMemberIds(heartMemberIds)
                .scrapMemberIds(scrapMemberIds)
                .commentResDtoList(commentResDtoList)
                .build();
    }

}
