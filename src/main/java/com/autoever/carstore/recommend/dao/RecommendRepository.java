package com.autoever.carstore.recommend.dao;

import com.autoever.carstore.recommend.entity.RecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendRepository  extends JpaRepository<RecommendEntity, Long> {
    @Query("SELECT r FROM RecommendEntity r WHERE r.user.userId = :userId")
    RecommendEntity findByUserId(@Param("userId") long userId);

    @Query("SELECT DISTINCT r.user.userId FROM RecommendEntity r")
    List<Long> findAllUserIds();
}