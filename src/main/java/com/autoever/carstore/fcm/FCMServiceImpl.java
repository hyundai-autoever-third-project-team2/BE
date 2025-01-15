package com.autoever.carstore.fcm;

import com.autoever.carstore.fcm.dto.NotificationRequest;
import com.autoever.carstore.fcm.dto.NotificationResponse;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService {

    @Override
    public NotificationResponse sendMessage(NotificationRequest request) {
        try {
            Message message = Message.builder()
                    .setToken(request.getToken())
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build())
                    .putAllData(request.getData())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            return new NotificationResponse(1, 0, response);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM message to token: {}", request.getToken(), e);
            return new NotificationResponse(0, 1, null);
        }
    }
    @Override
    public NotificationResponse sendMessageToMany(List<String> tokens, NotificationRequest request) {
        try {
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build())
                    .putAllData(request.getData())
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);

            return new NotificationResponse(
                    response.getSuccessCount(),
                    response.getFailureCount(),
                    response.getResponses().toString()
            );
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM messages to tokens: {}", tokens, e);
            return new NotificationResponse(0, tokens.size(), null);
        }
    }

    // 토픽 기반 메시지 전송
    @Override
    public NotificationResponse sendMessageToTopic(String topic, NotificationRequest request) {
        try {
            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build())
                    .putAllData(request.getData())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            return new NotificationResponse(1, 0, response);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM message to topic: {}", topic, e);
            return new NotificationResponse(0, 1, null);
        }
    }
}