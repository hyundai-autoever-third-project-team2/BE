package com.autoever.carstore.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Component;

@Component
public class FCMUtil {
    public void sendNotification(String fcmToken, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()  // Data가 아닌 Notification으로 변경
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("click_action", "OPEN_ACTIVITY")  // 앱 실행 시 동작 정의
                    .putData("type", "notification")          // 알림 타입 정의
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send FCM notification", e);
        }
    }
}
