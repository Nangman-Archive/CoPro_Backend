package com.example.copro.auth.api.dto.response;

public record UserInfo(
        String email,
        String name,
        String picture
) {
}
