package com.example.copro.board.api.dto.request;


import com.example.copro.board.domain.Category;
import java.util.List;

public record BoardSaveReqDto(
        String title,
        Category category,
        String contents,
        String tag,
        int count,
        int heart,
        List<Long> imageId
) {
}
