package com.example.copro.board.api.dto.request;


import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.Part;

import java.util.List;

public record BoardSaveReqDto(
        String title,
        Category category,
        String contents,
        String summary,
        Part part,
        String tag,
        List<Long> imageId
) {
}
