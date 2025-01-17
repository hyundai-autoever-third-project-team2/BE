package com.autoever.carstore.notification.service;

import com.autoever.carstore.notification.dto.NotificationRequestDto;
import com.autoever.carstore.notification.dto.NotificationResponseDto;

import java.util.List;

public interface NotificationService {
    NotificationResponseDto addNotification(NotificationRequestDto request);

    void clickNotification(Long notificationId);

    List<NotificationResponseDto> getNotificationList();
}
