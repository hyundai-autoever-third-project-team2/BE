//package com.autoever.carstore.fcm;
//
//import com.autoever.carstore.fcm.dto.NotificationRequest;
//import com.autoever.carstore.fcm.dto.NotificationResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/notifications")
//@RequiredArgsConstructor
//public class FCMController {
//    private final FCMService fcmService;
//
//    @PostMapping("/send")
//    public ResponseEntity<NotificationResponse> sendNotification(
//            @RequestBody NotificationRequest request) {
//        NotificationResponse response = fcmService.sendMessage(request);
//        return ResponseEntity.ok(response);
//    }
////
////    @PostMapping("/send/many")
////    public ResponseEntity<NotificationResponse> sendNotificationToMany(
////            @RequestBody List<String> tokens,
////            @RequestBody NotificationRequest request) {
////        NotificationResponse response = fcmService.sendMessageToMany(tokens, request);
////        return ResponseEntity.ok(response);
////    }
//
//    @PostMapping("/send/topic/{topic}")
//    public ResponseEntity<NotificationResponse> sendNotificationToTopic(
//            @PathVariable String topic,
//            @RequestBody NotificationRequest request) {
//        NotificationResponse response = fcmService.sendMessageToTopic(topic, request);
//        return ResponseEntity.ok(response);
//    }
//}
