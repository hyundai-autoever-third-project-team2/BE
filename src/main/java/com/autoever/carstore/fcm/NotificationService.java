package com.autoever.carstore.fcm;

import com.autoever.carstore.fcm.dto.NotificationRequest;
import com.autoever.carstore.fcm.dto.NotificationResponse;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FCMUtil fcmUtil;

    public void notifyEvent(String fcmToken, String title, String body) {
        // Logic to handle specific event
        fcmUtil.sendNotification(fcmToken, title, body);
    }
}