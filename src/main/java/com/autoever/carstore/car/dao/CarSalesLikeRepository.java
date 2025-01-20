package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarModelEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.car.entity.CarSalesLikeEntity;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    List<CarSalesLikeEntity> findByCarSales(CarSalesEntity carSales);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM CarSalesLikeEntity c " +
            "WHERE c.carSales.carSalesId = :carSalesId AND c.user.userId = :userId")
    boolean findByCarSalesIdUserId(long carSalesId, long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CarSalesLikeEntity  c WHERE c.user.userId = :userId AND c.carSales.carSalesId  = :carSalesId")
    void deleteByUserIdCarId(@Param("userId") long userId, @Param("carSalesId") long carSalesId);

    @Query("SELECT csl.user " +
            "FROM CarSalesLikeEntity csl " +
            "JOIN csl.carSales cs " +
            "JOIN cs.car c " +
            "WHERE c.carModel = :carModel ")
    List<UserEntity> findUsersByCarModelId(@Param("carModel") CarModelEntity carModel);
}
