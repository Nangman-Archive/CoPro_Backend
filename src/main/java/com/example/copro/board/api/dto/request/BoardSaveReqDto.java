package com.example.copro.board.api.dto.request;


import com.example.copro.board.domain.Category;
import com.example.copro.board.domain.Part;
import com.example.copro.board.domain.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BoardSaveReqDto(
        @NotBlank
        String title,
        @NotNull
        Category category,
        @NotBlank
        String contents,
        @NotNull
        Part part,
        @NotNull
        Tag tag,
        @NotEmpty
        List<Long> imageId
) {
}
