package com.autoever.carstore.notification.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private long notificationId;
    private int notificationType;
    private String title;
    private String content;
    private boolean isRead;
}
