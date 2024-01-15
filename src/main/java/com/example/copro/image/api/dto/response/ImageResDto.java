package com.example.copro.image.api.dto.response;

import com.example.copro.board.api.common.ConstantClass;
import com.example.copro.board.domain.Board;
import com.example.copro.image.domain.Image;
import lombok.Builder;

import java.util.List;

@Builder
public record ImageResDto(
        Long imageId,
        String imageUrl
) {
    public static ImageResDto of(Image image) {
        return ImageResDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
