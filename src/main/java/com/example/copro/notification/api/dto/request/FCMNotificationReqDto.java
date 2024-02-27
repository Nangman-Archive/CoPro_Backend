package com.example.copro.notification.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.Builder;

@Builder
public record FCMNotificationReqDto(
        @NotBlank
        String targetMemberEmail,
        @NotBlank
        String title,
        @NotBlank
        String body,

        Map<String, String> data
) {
}
