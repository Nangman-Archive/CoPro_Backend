package com.example.copro.auth.api.dto.request;

public record CodeReqDto(
        String code,
        String redirectUri
){
}
