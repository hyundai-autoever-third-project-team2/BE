package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarPurchaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarPurchaseRepository  extends JpaRepository<CarPurchaseEntity, Long> {
    @Query("SELECT c FROM CarPurchaseEntity c WHERE c.user.userId = :userId " +
            "ORDER BY c.purchaseDate DESC")
    List<CarPurchaseEntity> findByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(c) FROM CarPurchaseEntity c WHERE c.user.userId = :userId")
    int countByUserId(@Param("userId") long userId);

    @Query("SELECT c FROM CarPurchaseEntity c WHERE c.carPurchaseId = :carPurchaseId")
    CarPurchaseEntity findByCarPurchaseId(@Param("carPurchaseId") long carPurchaseId);

    @Query("SELECT c FROM CarPurchaseEntity c WHERE c.progress = :progress AND c.isDeleted = false")
    Page<CarPurchaseEntity> findByProgressAndIsDeleteFalse(@Param("progress") String progress, Pageable pageable);

    @Query("SELECT c FROM CarPurchaseEntity c WHERE c.progress = :progress")
    Page<CarPurchaseEntity> findByProgress(String progress, Pageable pageable);
}

