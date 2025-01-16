package com.autoever.carstore.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    //입장을 할 때 사용하는 루트입니다.
    @MessageMapping("/chat/enter")
    public void enter(@RequestBody ChatRequestDto dto) {
        messagingTemplate.convertAndSend("/sub/chat/room/1", dto);
    }

    //메세지를 전송할 때 사용하는 루트입니다.
    @MessageMapping("/chat/message")
    public void message(@RequestBody ChatRequestDto dto) {
        messagingTemplate.convertAndSend("/sub/chat/room/1", dto);
    }

}