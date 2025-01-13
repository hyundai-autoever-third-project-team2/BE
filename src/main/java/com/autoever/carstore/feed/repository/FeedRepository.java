package com.autoever.carstore.feed.repository;

import com.autoever.carstore.feed.entity.FeedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<FeedEntity, Long> {
    FeedEntity save(FeedEntity feed);
    List<FeedEntity> findByIsDeletedFalseOrderByCreatedAtDesc();
}
