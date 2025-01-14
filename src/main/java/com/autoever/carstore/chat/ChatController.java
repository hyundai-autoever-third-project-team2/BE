package com.autoever.carstore.chat;

import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final UserRepository userRepository;

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        UserEntity user = userRepository.findById(chatMessage.getSender())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity user = userRepository.findById(5L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        chatMessage.setSender(user.getNickname());
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessage;
    }

    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public ChatMessage joinMessage(@Payload ChatMessage chatMessage) {
//        UserEntity user = userRepository.findById(chatMessage.getSender())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity user = userRepository.findById(5L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        chatMessage.setSender(user.getNickname());
        chatMessage.setType("JOIN");
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessage;
    }
}
