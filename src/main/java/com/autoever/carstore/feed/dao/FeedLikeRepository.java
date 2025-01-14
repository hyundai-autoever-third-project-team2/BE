package com.autoever.carstore.feed.dao;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.feed.entity.FeedLikeEntity;
import com.autoever.carstore.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedLikeRepository extends JpaRepository<FeedLikeEntity, Long> {
    void deleteByUserAndFeed(UserEntity user, FeedEntity feed);
    boolean existsByUserAndFeed(UserEntity user, FeedEntity feed);
}
