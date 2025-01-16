package com.autoever.carstore.chat.entity;

import com.autoever.carstore.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity receiver;

    private String message;
    private LocalDateTime sentAt;
}