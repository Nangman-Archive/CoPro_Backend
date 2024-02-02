package com.example.copro.board.api.dto.request;


import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.Part;
import com.example.copro.board.domain.Tag;

import java.util.List;

public record BoardSaveReqDto(
        String title,
        Category category,
        String contents,
        String summary,
        Part part,
        Tag tag,
        List<Long> imageId
) {
}
