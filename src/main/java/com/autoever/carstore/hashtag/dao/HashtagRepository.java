package com.autoever.carstore.hashtag.dao;

import com.autoever.carstore.hashtag.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {
    Optional<HashtagEntity> findByHashtag(String hashtag);
    Boolean existsByHashtag(String hashtag);
}
