package com.autoever.carstore.chat.dao;

import com.autoever.carstore.chat.entity.ChatMessage;
import com.autoever.carstore.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatMessageId IN " +
            "(SELECT MAX(c.chatMessageId) FROM ChatMessage c GROUP BY c.sender)")
    List<ChatMessage> findLatestMessagesByUser();

    List<ChatMessage> findBySenderAndReceiverOrderBySentAtDesc(UserEntity sender, UserEntity receiver);
}
