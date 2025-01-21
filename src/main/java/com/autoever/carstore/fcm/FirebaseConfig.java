package com.autoever.carstore.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Log4j2
@Configuration
public class FirebaseConfig {
    private GoogleCredentials googleCredentials;

    @Value("${firebase.config.path:firebase/carstore-d56d2-firebase-adminsdk-a1pun-726834efe5.json}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initializeFirebase() {
        try {
            InputStream serviceAccount = null;
            try {
                // 1. 먼저 클래스패스에서 시도
                ClassPathResource resource = new ClassPathResource(firebaseConfigPath);
                serviceAccount = resource.getInputStream();
                log.info("Firebase 설정 파일을 클래스패스에서 찾았습니다: {}", firebaseConfigPath);
            } catch (IOException e) {
                // 2. 실패시 파일 시스템에서 시도
                log.warn("클래스패스에서 Firebase 설정 파일을 찾지 못했습니다. 파일 시스템에서 시도합니다.");
                serviceAccount = new FileInputStream(firebaseConfigPath);
                log.info("Firebase 설정 파일을 파일 시스템에서 찾았습니다: {}", firebaseConfigPath);
            }

            googleCredentials = GoogleCredentials
                    .fromStream(serviceAccount)
                    .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase 초기화 성공");
            }
        } catch (IOException e) {
            log.error("Firebase 초기화 실패: {}", e.getMessage(), e);
            throw new RuntimeException("Firebase 초기화 실패", e);
        }
    }

    public GoogleCredentials getGoogleCredentials() {
        if (googleCredentials == null) {
            log.error("Google Credentials가 초기화되지 않았습니다.");
            throw new RuntimeException("Google Credentials가 초기화되지 않았습니다.");
        }
        return this.googleCredentials;
    }
}