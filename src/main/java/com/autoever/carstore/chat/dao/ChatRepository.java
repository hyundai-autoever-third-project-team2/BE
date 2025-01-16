package com.autoever.carstore.chat.dao;

import com.autoever.carstore.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    @Query("SELECT c FROM ChatEntity c WHERE c.room_id = :roomId ORDER BY c.timestamp DESC")
    List<ChatEntity> findByRoomIdOrderByTimestampDesc(long roomId);
}