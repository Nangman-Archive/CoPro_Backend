package com.example.copro.notification.api.dto.request;

import lombok.Builder;

@Builder
public record FCMNotificationReqDto(
        String targetMemberEmail,
        String title,
        String body
) {
}
