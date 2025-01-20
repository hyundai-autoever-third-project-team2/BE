package com.autoever.carstore.notification.dao;

import com.autoever.carstore.notification.entity.NotificationEntity;
import com.autoever.carstore.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserOrderByCreatedAtDesc(UserEntity user);
}
