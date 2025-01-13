package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {

}
