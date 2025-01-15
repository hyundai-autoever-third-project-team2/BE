package com.autoever.carstore.chat;

import com.autoever.carstore.user.dto.response.UserResponseDto;
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

//    private MessageType messageType;
//    private Long chatRoomId;
//    private String message;
//    private String senderId;

    private MessageType type;
//    private Long senderId;
    private Long receiverId;
    private String message;
}