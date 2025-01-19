package com.autoever.carstore.notification.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private long notificationId;
    private String notificationType;
    private String title;
    private String content;
    private boolean isRead;
    private LocalDateTime receivedTime;
}
