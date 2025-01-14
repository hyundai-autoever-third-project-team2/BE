package com.autoever.carstore.recommend.dao;

import com.autoever.carstore.recommend.entity.RecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository  extends JpaRepository<RecommendEntity, Long> {

}