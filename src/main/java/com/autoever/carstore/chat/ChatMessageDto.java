package com.autoever.carstore.chat;

import com.autoever.carstore.user.dto.response.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    // 메시지  타입 : 입장, 채팅, 퇴장
//    public enum MessageType{
//        JOIN, TALK, LEAVE
//    }

//    private MessageType messageType;
//    private Long chatRoomId;
//    private String message;
//    private String senderId;

//    private MessageType messageType;
    private UserResponseDto sender;  // 메시지를 보낸 사람
    private UserResponseDto receiver; // 메시지를 받는 사람
    private String message;
    private LocalDateTime sentAt;
}