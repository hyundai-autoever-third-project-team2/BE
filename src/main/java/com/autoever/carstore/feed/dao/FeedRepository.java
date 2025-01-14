package com.autoever.carstore.feed.dao;

import com.autoever.carstore.feed.entity.FeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
    FeedEntity save(FeedEntity feed);
    List<FeedEntity> findByIsDeletedFalseOrderByCreatedAtDesc();
}
