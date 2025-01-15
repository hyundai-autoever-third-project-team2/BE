package com.autoever.carstore.fcm;

import com.autoever.carstore.fcm.dto.NotificationRequest;
import com.autoever.carstore.fcm.dto.NotificationResponse;

import java.util.List;

public interface FCMService {
    public NotificationResponse sendMessage(NotificationRequest request);

    public NotificationResponse sendMessageToMany(List<String> tokens, NotificationRequest request);

    // 토픽 기반 메시지 전송
    public NotificationResponse sendMessageToTopic(String topic, NotificationRequest request);
}
