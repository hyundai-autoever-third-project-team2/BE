//package com.autoever.carstore.chat;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Entity
//@Table(name="chat_message")
//public class ChatEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "chat_message_id", unique = true, nullable = false)
//    long chat_id;
//    @Column(name = "room_id", nullable = false)
//    long room_id;
//
//
//    String sender;
//    String message;
//    LocalDateTime timestamp;
//}
