package com.autoever.carstore.notification.controller;

import com.autoever.carstore.notification.dto.NotificationResponseDto;
import com.autoever.carstore.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PutMapping("/click")
    public ResponseEntity<?> register(@RequestBody Map<String, Long> request) {
        Long notificationId = request.get("notificationId");

        notificationService.clickNotification(notificationId);

        return ResponseEntity.ok("success");
    }

    @GetMapping("/list")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationList() {
        List<NotificationResponseDto> result = notificationService.getNotificationList();

        return ResponseEntity.ok(result);
    }
}
