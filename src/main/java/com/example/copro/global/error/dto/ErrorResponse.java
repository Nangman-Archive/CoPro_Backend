package com.example.copro.global.error.dto;

public record ErrorResponse(
        int statusCode,
        String message
) {
}