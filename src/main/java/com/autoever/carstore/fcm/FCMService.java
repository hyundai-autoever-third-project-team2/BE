package com.autoever.carstore.fcm;

import java.util.List;

public interface FCMService {
    public void sendMessage(String token, String title, String body);
    // 다중 사용자에게 전송
    public void sendMessageToMany(List<String> tokens, String title, String body);
}
