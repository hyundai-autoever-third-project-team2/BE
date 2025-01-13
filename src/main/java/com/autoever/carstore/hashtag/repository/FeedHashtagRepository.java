package com.autoever.carstore.hashtag.repository;

import com.autoever.carstore.feed.entity.FeedEntity;
import com.autoever.carstore.hashtag.entity.FeedHashtagEntity;
import com.autoever.carstore.hashtag.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedHashtagRepository extends JpaRepository<FeedHashtagEntity, Long> {
    List<FeedHashtagEntity> findByFeed(FeedEntity feed);
    Boolean existsByFeedAndHashtag(FeedEntity feed, HashtagEntity hashtag);
}
