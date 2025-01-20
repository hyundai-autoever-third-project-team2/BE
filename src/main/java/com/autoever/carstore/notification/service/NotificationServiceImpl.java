package com.autoever.carstore.notification.service;

import com.autoever.carstore.notification.dao.NotificationRepository;
import com.autoever.carstore.notification.dto.NotificationRequestDto;
import com.autoever.carstore.notification.dto.NotificationResponseDto;
import com.autoever.carstore.notification.entity.NotificationEntity;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SecurityUtil securityUtil;

    @Override
    public NotificationResponseDto addNotification(NotificationRequestDto request) {
        String type = "";

        switch(request.getNotificationType()){
            case 0:
                type = "result";
                break;
            case 1:
                type = "wishlist";
                break;
            case 2:
                type = "discount";
                break;
            default:
                type = "recommend";
                break;
        }

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .user(request.getUser())
                .notificationType(request.getNotificationType())
                .title(request.getTitle())
                .content(request.getContent())
                .isRead(false)
                .build();

        NotificationEntity entity = notificationRepository.save(notificationEntity);

        return NotificationResponseDto.builder()
                .notificationId(entity.getNotificationId())
                .notificationType(type)
                .title(entity.getTitle())
                .content(entity.getContent())
                .isRead(entity.isRead())
                .receivedTime(entity.getCreatedAt())
                .build();
    }

    @Override
    public void clickNotification(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId).orElse(null);

        notification.updateIsRead();

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponseDto> getNotificationList() {
        UserEntity user = securityUtil.getLoginUser();

        List<NotificationEntity> list = notificationRepository.findByUserOrderByCreatedAtDesc(user);

        return list.stream()
                .map(notification ->
                        NotificationResponseDto.builder()
                                .notificationId(notification.getNotificationId())
                                .notificationType(notification.getNotificationType() == 0 ? "result" :
                                        (notification.getNotificationType() == 1 ? "wishlist" : "discount"))
                                .title(notification.getTitle())
                                .content(notification.getContent())
                                .isRead(notification.isRead())
                                .receivedTime(notification.getCreatedAt())
                                .build())
                .collect(Collectors.toList());
    }
}
