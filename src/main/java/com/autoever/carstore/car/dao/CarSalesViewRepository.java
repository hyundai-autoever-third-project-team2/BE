package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarSalesViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarSalesViewRepository extends JpaRepository<CarSalesViewEntity, Long> {
    @Query("SELECT COUNT(c) FROM CarSalesViewEntity c WHERE c.carSales.carSalesId = :carSalesId")
    int getCountByCarSalesId(@Param("carSalesId") long carSalesId);
}
