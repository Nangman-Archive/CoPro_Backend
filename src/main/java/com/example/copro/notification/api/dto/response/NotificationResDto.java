package com.example.copro.notification.api.dto.response;

import com.example.copro.notification.domain.Notification;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NotificationResDto(
        String message,
        Long boardId,
        LocalDateTime createAt
) {
    public static NotificationResDto from(Notification notification) {
        return NotificationResDto.builder()
                .message(notification.getMessage())
                .boardId(notification.getBoardId())
                .createAt(notification.getCreateAt())
                .build();
    }
}
