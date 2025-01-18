package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarPurchaseRepository  extends JpaRepository<CarPurchaseEntity, Long> {
    @Query("SELECT c FROM CarPurchaseEntity c WHERE c.user.userId = :userId " +
            "ORDER BY c.purchaseDate DESC")
    List<CarPurchaseEntity> findByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(c) FROM CarPurchaseEntity c WHERE c.user.userId = :userId")
    int countByUserId(@Param("userId") long userId);

    @Query("SELECT c FROM CarPurchaseEntity c WHERE c.carPurchaseId = :carPurchaseId")
    CarPurchaseEntity findByCarPurchaseId(@Param("carPurchaseId") long carPurchaseId);

    List<CarPurchaseEntity> findByProgress(String progress);
}

