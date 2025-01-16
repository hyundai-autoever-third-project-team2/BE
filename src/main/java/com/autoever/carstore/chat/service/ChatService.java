package com.autoever.carstore.chat.service;

import com.autoever.carstore.chat.dao.ChatRepository;
import com.autoever.carstore.chat.entity.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatMessageRepository;

    // 메시지를 DB에 저장
    public void saveMessage(long roomId, String sender, String content) {
        ChatEntity chatMessage = ChatEntity.builder()
                .room_id(roomId)
                .sender(sender)
                .content(content)
                .build();
        chatMessageRepository.save(chatMessage);
    }

    // 특정 채팅방의 이전 메시지 로드
    public List<ChatEntity> getMessagesByRoomId(long roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampDesc(roomId);
    }
}