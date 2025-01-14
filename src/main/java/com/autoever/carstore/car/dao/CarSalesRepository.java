package com.autoever.carstore.car.dao;

import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.car.entity.CarSalesLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarSalesRepository extends JpaRepository<CarSalesEntity, Long> {
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN FETCH cs.car c " +
            "JOIN FETCH c.carModel cm " +
            "WHERE cm.brand = :brand")
    List<CarSalesEntity> findAllWithCarAndCarModelByBrand(@Param("brand") String brand);

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN FETCH cs.car c " +
            "JOIN FETCH c.carModel cm " +
            "WHERE cm.brand NOT IN :brands")
    List<CarSalesEntity> findAllWithCarAndCarModelExcludingBrands(@Param("brands") List<String> brands);


    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN FETCH cs.car c " +
            "JOIN FETCH c.carModel cm " +
            "WHERE cm.brand IN :brands")
    List<CarSalesEntity> findAllWithCarAndCarModelByBrands(@Param("brands") List<String> brands);

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "ORDER BY cs.createdAt DESC")
    List<CarSalesEntity> findAllLately();

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "LEFT JOIN cs.likes likes " +
            "GROUP BY cs " +
            "ORDER BY COUNT(likes) DESC")
    List<CarSalesEntity> findTop50ByOrderByLikesDesc(Pageable pageable);

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "WHERE cs.createdAt < :oneWeekAgo")
    List<CarSalesEntity> findAllOlderThanAWeek(@Param("oneWeekAgo") LocalDateTime oneWeekAgo);

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "LEFT JOIN cs.likes likes " +
            "GROUP BY cs " +
            "ORDER BY COUNT(likes) DESC")
    List<CarSalesEntity> findByOrderByLikesDesc();

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE (:brand IS NULL OR cm.brand LIKE %:brand%) " +
            "AND (:modelName IS NULL OR cm.modelName LIKE %:modelName%)")
    List<CarSalesEntity> findByBrandAndCarName(@Param("brand") String brand, @Param("modelName") String modelName);

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE (:carTypes IS NULL OR cm.carType IN :carTypes) " +
            "AND (cm.displacement >= :startDisplacement) " +
            "AND (cm.displacement <= :endDisplacement) " +
            "AND (c.distance >= :startPrice) " +
            "AND (c.distance <= :endPrice) " +
            "AND (:colors IS NULL OR c.color IN :colors)")
    List<CarSalesEntity> filterCars(@Param("carTypes") List<String> carTypes,
                                    @Param("startDisplacement") int startDisplacement,
                                    @Param("endDisplacement") int endDisplacement,
                                    @Param("startPrice") int startPrice,
                                    @Param("endPrice") int endPrice,
                                    @Param("colors") List<String> colors);

    @Query("SELECT cs FROM CarSalesEntity cs WHERE cs.car.carId = :carId")
    CarSalesEntity findByCarId(Long carId);

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE cm.carType = :carType " +
            "AND cm.brand = :brand")
    List<CarSalesEntity> findSimilarCar(String carType, String brand);

    @Query("SELECT c FROM CarSalesEntity c WHERE c.user.userId = :userId AND c.progress = :progress")
    List<CarSalesEntity> findByUserIdAndProgress(@Param("userId") long userId, @Param("progress") String progress);

}