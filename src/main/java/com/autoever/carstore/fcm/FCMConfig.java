//package com.autoever.carstore.fcm;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.IOException;
//
//@Slf4j
//@Configuration
//public class FCMConfig {
//    @Value("${fcm.certification}")
//    private String googleApplicationCredentials;
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(googleApplicationCredentials).getInputStream()))
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//            }
//        } catch (IOException e) {
//            log.error("FCM 초기화 실패", e);
//        }
//    }
//}
