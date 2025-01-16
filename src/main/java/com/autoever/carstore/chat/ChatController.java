package com.autoever.carstore.chat;

import com.autoever.carstore.chat.dao.ChatMessageRepository;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageRepository chatRepository;
    private final UserService userService;

    // 관리자용 - 모든 채팅방(사용자별 최신 메시지) 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatMessageDto>> getChatRooms(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (!isAdmin(userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(
                chatRepository.findLatestMessagesByUser().stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList())
        );
    }

    // 특정 사용자와의 채팅 내역 조회
    @GetMapping("/rooms/{userId}")
    public ResponseEntity<List<ChatMessageDto>> getChatHistory(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        User otherUser = userService.findById(userId);

        return ResponseEntity.ok(
                chatRepository.findBySenderAndReceiverOrderBySentAtDesc(
                                currentUser, otherUser).stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList())
        );
    }
}