package com.autoever.carstore.chat;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    // 메시지  타입 : 입장, 채팅, 퇴장
    public enum MessageType{
        JOIN, TALK, LEAVE
    }

    private MessageType messageType;
    private Long chatRoomId;
    private String message;
    private String senderId;
//    private UserResponseDto sender;
    private String receiverId;
}