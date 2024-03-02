package com.example.copro.image.api.dto.request;

import java.util.List;

public record ImageReqDto(
        List<Long> imageIds
) {
}
