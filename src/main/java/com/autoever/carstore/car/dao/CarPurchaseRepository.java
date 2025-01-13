package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarPurchaseRepository  extends JpaRepository<CarPurchaseEntity, Long> {

}

