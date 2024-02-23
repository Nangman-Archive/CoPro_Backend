package com.example.copro.notification.api.dto.response;

import com.example.copro.notification.domain.Notification;
import lombok.Builder;

@Builder
public record NotificationResDto(
        String message,
        Long boardId
) {
    public static NotificationResDto from(Notification notification) {
        return NotificationResDto.builder()
                .message(notification.getMessage())
                .boardId(notification.getBoardId())
                .build();
    }
}
