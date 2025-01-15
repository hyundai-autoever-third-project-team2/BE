//package com.autoever.carstore.chat;
//
//import com.autoever.carstore.user.dao.UserRepository;
//import com.autoever.carstore.user.dto.response.UserResponseDto;
//import com.autoever.carstore.user.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class WebSocketChatHandler extends TextWebSocketHandler {
//    private final ObjectMapper mapper;
//    private final UserService userService;
//    private final UserRepository userRepository;
//
//    private final Set<WebSocketSession> sessions = new HashSet<>();
//    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
//    private final Map<WebSocketSession, UserResponseDto> sessionUserMap = new HashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        SecurityContext context = SecurityContextHolder.getContext();
//        if (context.getAuthentication() != null) {
//            UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
//            UserResponseDto userInfo = userService.getUserInfo(userDetails.getUsername());
//            sessionUserMap.put(session, userInfo);
//        }
//        sessions.add(session);
//        session.sendMessage(new TextMessage("WebSocket 연결 완료"));
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
//        chatMessageDto.setSender(sessionUserMap.get(session));
//
//        Long roomId = chatMessageDto.getChatRoomId();
//
//        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.JOIN)) {
//            if (!chatRoomSessionMap.containsKey(roomId)) {
//                chatRoomSessionMap.put(roomId, new HashSet<>());
//            }
//            chatRoomSessionMap.get(roomId).add(session);
//            chatMessageDto.setMessage(chatMessageDto.getSender().getNickname() + "님이 입장하셨습니다.");
//            broadcastMessageToRoom(roomId, session, chatMessageDto);
//
//        } else if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.TALK)) {
//            if (chatRoomSessionMap.containsKey(roomId)) {
//                broadcastMessageToRoom(roomId, session, chatMessageDto);
//            }
//        } else if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.LEAVE)) {
//            if (chatRoomSessionMap.containsKey(roomId)) {
//                chatRoomSessionMap.get(roomId).remove(session);
//                chatMessageDto.setMessage(chatMessageDto.getSender().getNickname() + "님이 퇴장하셨습니다.");
//                broadcastMessageToRoom(roomId, session, chatMessageDto);
//
//                if (chatRoomSessionMap.get(roomId).isEmpty()) {
//                    chatRoomSessionMap.remove(roomId);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        log.info("{} 연결 끊김", session.getId());
//        sessionUserMap.remove(session);
//        chatRoomSessionMap.values().forEach(sessions -> sessions.remove(session));
//        sessions.remove(session);
//    }
//
//    private void broadcastMessageToRoom(Long roomId, WebSocketSession senderSession, ChatMessageDto chatMessageDto) throws Exception {
//        Set<WebSocketSession> roomSessions = chatRoomSessionMap.getOrDefault(roomId, Set.of());
//        for (WebSocketSession client : roomSessions) {
//            if (client.isOpen() && !client.getId().equals(senderSession.getId())) {
//                client.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessageDto)));
//            }
//        }
//    }
//}
package com.autoever.carstore.chat;

import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.user.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
//public class WebSocketChatHandler extends TextWebSocketHandler {
//    private final ObjectMapper mapper;
//
//    // 소켓 세션을 저장할 Set
//    private final Set<WebSocketSession> sessions = new HashSet<>();
//
//    // 채팅방 id와 소켓 세션을 저장할 Map
//    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
//
//    // 소켓 연결 확인
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        log.info("{} 연결됨", session.getId());
//        sessions.add(session);
//        session.sendMessage(new TextMessage("WebSocket 연결 완료"));
//    }
//
//    // 소켓 메시지 처리
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
//
//        Long roomId = chatMessageDto.getChatRoomId();
//
//        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.JOIN)) {
//            if (!chatRoomSessionMap.containsKey(roomId)) {
//                chatRoomSessionMap.put(roomId, new HashSet<>());
//            }
//            chatRoomSessionMap.get(roomId).add(session);
////            chatMessageDto.setMessage(chatMessageDto.getSenderId() + "님이 입장하셨습니다.");
////
////            // 다른 사용자들에게 입장 알림
////            broadcastMessageToRoom(roomId, session, chatMessageDto);
//
//        } else if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.TALK)) {
//            if (chatRoomSessionMap.containsKey(roomId)) {
//                broadcastMessageToRoom(roomId, session, chatMessageDto);
//            }
//        } else if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.LEAVE)) {
//            if (chatRoomSessionMap.containsKey(roomId)) {
//                chatRoomSessionMap.get(roomId).remove(session);
////                chatMessageDto.setMessage(chatMessageDto.getSenderId() + "님이 퇴장하셨습니다.");
////
////                // 다른 사용자들에게 퇴장 알림
////                broadcastMessageToRoom(roomId, session, chatMessageDto);
//
//                // 방이 비어 있으면 삭제
//                if (chatRoomSessionMap.get(roomId).isEmpty()) {
//                    chatRoomSessionMap.remove(roomId);
//                }
//            }
//        }
//    }
//
//    // 소켓 연결 종료
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        log.info("{} 연결 끊김", session.getId());
//
//        // 모든 방에서 해당 세션 제거
//        chatRoomSessionMap.values().forEach(sessions -> sessions.remove(session));
//    }
//
//    // 채팅방에 메시지 브로드캐스트
//    private void broadcastMessageToRoom(Long roomId, WebSocketSession senderSession, ChatMessageDto chatMessageDto) throws Exception {
//        Set<WebSocketSession> roomSessions = chatRoomSessionMap.getOrDefault(roomId, Set.of());
//        for (WebSocketSession client : roomSessions) {
//            if (client.isOpen() && !client.getId().equals(senderSession.getId())) {
//                client.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessageDto)));
//            }
//        }
//    }
//}
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;
    private final Map<String, WebSocketSession> userSessions = new HashMap<>(); // senderId 기반 세션 저장
    private final SecurityUtil securityUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UserEntity user = securityUtil.getLoginUser();
//        String senderId = session.getAttributes().get("senderId").toString(); // 연결 시 사용자 ID
//        userSessions.put(senderId, session);
        userSessions.put(user.getNickname(), session);
        log.info("User connected: {}", user.getNickname());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UserEntity user = securityUtil.getLoginUser();
        ChatMessageDto chatMessage = mapper.readValue(message.getPayload(), ChatMessageDto.class);
        WebSocketSession receiverSession = userSessions.get(chatMessage.getReceiverId()); // 대상 세션 찾기

        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessage)));
//            log.info("Message sent from {} to {}", chatMessage.getSenderId(), chatMessage.getReceiverId());
            log.info("Message sent from {} to {}", user.getNickname(), chatMessage.getReceiverId());
        } else {
            log.warn("Receiver not connected: {}", chatMessage.getReceiverId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UserEntity user = securityUtil.getLoginUser();
//        String senderId = session.getAttributes().get("sender").toString();
//        userSessions.remove(senderId);
        userSessions.remove(user.getNickname());
        log.info("User disconnected: {}", user.getNickname());
    }
}