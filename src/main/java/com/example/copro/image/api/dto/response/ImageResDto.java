package com.example.copro.image.api.dto.response;

import com.example.copro.image.domain.Image;
import lombok.Builder;

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
