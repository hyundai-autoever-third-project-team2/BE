package com.autoever.carstore.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String type;    // MESSAGE, JOIN, LEAVE
    private String content;
    private String sender;
    private String receiver;
    private LocalDateTime timestamp;
}
