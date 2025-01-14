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

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE (:carTypes IS NULL OR cm.carType IN :carTypes) " +
            "AND (cm.displacement >= :startDisplacement) " +
            "AND (cm.displacement <= :endDisplacement) " +
            "AND (c.distance >= :startDistance) " +
            "AND (c.distance <= :endDistance) " +
            "AND (cs.price >= :startPrice) " +
            "AND (cs.price <= :endPrice) " +
            "AND (:colors IS NULL OR c.color IN :colors)")
    List<CarSalesEntity> filterCars(List<String> carTypes, int startDisplacement, int endDisplacement, int startDistance, int endDistance, int startPrice, int endPrice, List<String> colors);

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE (c.distance >= :startDistance) " +
            "AND (c.distance <= :endDistance) " +
            "AND (cs.price >= :startPrice) " +
            "AND (cs.price <= :endPrice) " +
            "AND (cm.modelYear >= :minModelYear) " +
            "AND (cm.modelYear <= :maxModelYear) " +
            "AND (:carModelIds IS NULL OR cm.carModelId IN :carModelIds) " +
            "AND (:surveyColor IS NULL OR c.color IN :surveyColor)")
    List<CarSalesEntity> getAllRecommend(int startPrice, int endPrice, int startDistance, int endDistance, int minModelYear,
                                         int maxModelYear, List<Long> carModelIds, List<String> surveyColor);

    @Query("SELECT cs FROM CarSalesEntity cs WHERE cs.carSalesId = :carSalesId")
    CarSalesEntity findByCarSalesId(@Param("carSalesId") long carSalesId);

    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE (:brands IS NULL OR cm.brand IN :brands) " +
            "AND (:carTypes IS NULL OR cm.carType IN :carTypes)")
    List<CarSalesEntity> getAllRecommendByBrandsAndCarTypes(@Param("brands") List<String> brands,
                                                            @Param("carTypes") List<String> carTypes);
}