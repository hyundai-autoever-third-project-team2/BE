//package com.autoever.carstore.fcm;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/notifications")
//@RequiredArgsConstructor
//public class FCMController {
//    private final FCMService fcmService;
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
//        fcmService.sendMessage(request.getToken(), request.getTitle(), request.getBody());
//        return ResponseEntity.ok("Notification sent successfully");
//    }
//}
