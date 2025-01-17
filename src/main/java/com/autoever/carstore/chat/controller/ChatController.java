package com.autoever.carstore.chat.controller;

import com.autoever.carstore.chat.dto.ChatRequestDto;
import com.autoever.carstore.chat.entity.ChatEntity;
import com.autoever.carstore.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    // 입장 시 이전 메시지를 DB에서 불러와 전송
    @MessageMapping("/chat/enter")
    public void enter(ChatRequestDto dto) {
        long roomId = dto.getRoomId();
        // DB에서 이전 메시지 불러오기
        List<ChatEntity> messages = chatService.getMessagesByRoomId(roomId);

        // 이전 메시지 전송
        for (ChatEntity message : messages) {
            messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, message);
        }

        // 새로운 입장 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, dto);
    }

    // 메시지 전송 시 DB에 저장
    @MessageMapping("/chat/message")
    public void message(ChatRequestDto dto) {
        long roomId = dto.getRoomId();
        // DB에 메시지 저장
        chatService.saveMessage(roomId, dto.getSender(), dto.getContent());

        // 방에 메시지 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + roomId, dto);
    }
}