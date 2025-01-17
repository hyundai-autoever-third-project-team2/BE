package com.autoever.carstore.notification.dto;

import com.autoever.carstore.user.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDto {
    private UserEntity user;
    private int notificationType;
    private String title;
    private String content;
}
