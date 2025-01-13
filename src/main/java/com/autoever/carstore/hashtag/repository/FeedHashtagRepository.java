package com.autoever.carstore.hashtag.repository;

import com.autoever.carstore.hashtag.entity.FeedHashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedHashtagRepository extends JpaRepository<FeedHashtagEntity, Long> {

}
