package com.example.copro.notification.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FCMTokenReqDto (
        @NotBlank
        String fcmToken
){
}
