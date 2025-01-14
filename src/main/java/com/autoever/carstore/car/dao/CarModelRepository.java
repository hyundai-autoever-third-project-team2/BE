package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepository extends JpaRepository<CarModelEntity, Long> {

}