package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarPurchaseImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CarPurchaseImageRepository extends JpaRepository<CarPurchaseImageEntity, Long> {
    List<CarPurchaseImageEntity> findByCarPurchase_CarPurchaseId(Long carPurchaseId);
}