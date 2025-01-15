package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarPurchaseRepository  extends JpaRepository<CarPurchaseEntity, Long> {
    @Query("SELECT c FROM CarPurchaseEntity c WHERE c.user.userId = :userId AND c.progress = :progress")
    List<CarPurchaseEntity> findByUserIdAndProgress(@Param("userId") long userId, @Param("progress") String progress);

    @Query("SELECT COUNT(c) FROM CarPurchaseEntity c WHERE c.user.userId = :userId")
    int countByUserId(@Param("userId") long userId);
}

