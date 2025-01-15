package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarSalesLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarSalesLikeRepository extends JpaRepository<CarSalesLikeEntity, Long> {
    @Query("SELECT c FROM CarSalesLikeEntity c WHERE c.user.userId = :userId")
    List<CarSalesLikeEntity> findByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(c) FROM CarSalesLikeEntity c WHERE c.user.userId = :userId")
    int countByUserId(@Param("userId") long userId);
}
