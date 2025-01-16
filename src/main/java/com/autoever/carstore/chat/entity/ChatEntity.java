package com.autoever.carstore.chat.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name="chat_message")
public class ChatEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id", unique = true, nullable = false)
    long chat_id;

    @Column(name = "room_id", nullable = false)
    long room_id;
    @Column(name = "sender", nullable = false)
    String sender;
    @Column(name = "message", nullable = false)
    String message;

    @CreationTimestamp
    private Timestamp timestamp;
}
