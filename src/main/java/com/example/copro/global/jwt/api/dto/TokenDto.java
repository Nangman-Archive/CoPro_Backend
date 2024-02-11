package com.example.copro.global.jwt.api.dto;

import lombok.Builder;

@Builder
public record TokenDto (
        String accessToken,
        String refreshToken
) {

}
