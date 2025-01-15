package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarPurchaseImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarPurchaseImageRepository extends JpaRepository<CarPurchaseImageEntity, Long> {

}