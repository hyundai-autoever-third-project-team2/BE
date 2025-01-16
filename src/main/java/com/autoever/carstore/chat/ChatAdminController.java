package com.autoever.carstore.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatAdminController {
    private final WebSocketChatHandler webSocketChatHandler;

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatMessageDto>> getChatRooms() {
        return ResponseEntity.ok(webSocketChatHandler.getChatRooms());
    }
}
