package com.autoever.carstore.fcm.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FcmMessage {
    private boolean validateOnly;
    private FcmMessage.Message message;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private FcmMessage.Notification notification;
        private String token;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
