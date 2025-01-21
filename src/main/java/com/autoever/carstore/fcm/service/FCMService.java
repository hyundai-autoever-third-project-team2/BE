package com.autoever.carstore.fcm.service;

import com.autoever.carstore.fcm.dto.FcmMessage;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/carstore-d56d2/messages:send";
    private final ObjectMapper objectMapper;

    private GoogleCredentials googleCredentials;

    public void sendMessageTo(String targetToken, String title, String body) throws Exception {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, // 만든 message body에 넣기
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken()) // header에 포함
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();
        Response response = client.newCall(request).execute(); // 요청 보냄

        System.out.println(response.body().string());
    }

    // FCM 전송 정보를 기반으로 메시지를 구성한다. (Object -> String)
    private String makeMessage(String targetToken, String title, String body) throws com.fasterxml.jackson.core.JsonProcessingException  { // JsonParseException, JsonProcessingException
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .build()
                        )
                        .build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    // Firebase Admin SDK의 비공개 키를 참조하여 Bearer 토큰을 발급 받는다.
//    private String getAccessToken() throws Exception {
//        final String firebaseConfigPath = "firebase/carstore-d56d2-firebase-adminsdk-a1pun-726834efe5.json";
//
//        try {
//            final GoogleCredentials googleCredentials = GoogleCredentials
//                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
//                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
//
//            googleCredentials.refreshIfExpired();
//            log.info("access token: {}",googleCredentials.getAccessToken());
//            return googleCredentials.getAccessToken().getTokenValue();
//        } catch (IOException e) {
//            throw new Exception("no token");
//        }
//    }

    @PostConstruct
    private void initialize() {
        try {
            ClassPathResource resource = new ClassPathResource("firebase/carstore-d56d2-firebase-adminsdk-a1pun-ea2077f70a.json");
            googleCredentials = GoogleCredentials
                    .fromStream(resource.getInputStream())
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/firebase.messaging"));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            log.error("Firebase initialization failed", e);
        }
    }

    private String getAccessToken() throws Exception {
        try {
            String firebaseConfigPath = "firebase/carstore-d56d2-firebase-adminsdk-a1pun-ea2077f70a.json";
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(Arrays.asList(
                            "https://www.googleapis.com/auth/cloud-platform",
                            "https://www.googleapis.com/auth/firebase.messaging"
                    ));

            // 토큰 디버깅
            AccessToken token = googleCredentials.refreshAccessToken();
            String tokenValue = token.getTokenValue();
            log.info("Token successfully generated");

            return tokenValue;
        } catch (IOException e) {
            log.error("Failed to get access token: {}", e.getMessage(), e);
            throw new Exception("Failed to get access token: " + e.getMessage());
        }
    }

}