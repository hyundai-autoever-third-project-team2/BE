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

    //판매중인 해외 차량 조회
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN FETCH cs.car c " +
            "JOIN FETCH c.carModel cm " +
            "WHERE cs.progress = '판매중' " +
            "AND cm.brand NOT IN :brands")
    List<CarSalesEntity> findAllWithCarAndCarModelExcludingBrands(@Param("brands") List<String> brands);

    //판매중인 국내 차량 조회
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN FETCH cs.car c " +
            "JOIN FETCH c.carModel cm " +
            "WHERE cs.progress = '판매중' " +
            "AND cm.brand IN :brands")
    List<CarSalesEntity> findAllWithCarAndCarModelByBrands(@Param("brands") List<String> brands);

    //판매중인 최신순 차량 조회
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "WHERE cs.progress = '판매중' " +
            "ORDER BY cs.createdAt DESC")
    List<CarSalesEntity> findAllLately();

    //판매중인 인기순 top50 차량 조회
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "LEFT JOIN cs.likes likes " +
            "WHERE cs.progress = '판매중' " +
            "GROUP BY cs " +
            "ORDER BY COUNT(likes) DESC")
    List<CarSalesEntity> findTop50ByOrderByLikesDesc(Pageable pageable);

    //판매중인 할인 차량 조회
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "WHERE cs.createdAt < :oneWeekAgo " +
            "AND cs.progress = '판매중'")
    List<CarSalesEntity> findAllOlderThanAWeek(@Param("oneWeekAgo") LocalDateTime oneWeekAgo);

    //판매중인 인기순 차량 조회(전체보기에서의 필터링)
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "LEFT JOIN cs.likes likes " +
            "WHERE cs.progress = '판매중' " +
            "GROUP BY cs " +
            "ORDER BY COUNT(likes) DESC")
    List<CarSalesEntity> findByOrderByLikesDesc();

    //판매중인 검색 차량 조회(브랜드, 모델)
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE cs.progress = '판매중' " +
            "AND (:brand IS NULL OR cm.brand LIKE %:brand%) " +
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

    //판매중인 차량 카테고리 필터링
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE cs.progress = '판매중' " +
            "AND (:carTypes IS NULL OR cm.carType IN :carTypes) " +
            "AND (cm.displacement >= :startDisplacement) " +
            "AND (cm.displacement <= :endDisplacement) " +
            "AND (c.distance >= :startDistance) " +
            "AND (c.distance <= :endDistance) " +
            "AND (cs.price >= :startPrice) " +
            "AND (cs.price <= :endPrice) " +
            "AND (:colors IS NULL OR c.color IN :colors)")
    List<CarSalesEntity> filterCars(List<String> carTypes, int startDisplacement, int endDisplacement, int startDistance, int endDistance, int startPrice, int endPrice, List<String> colors);

    //판매중인 상품만 추천
    @Query("SELECT cs FROM CarSalesEntity cs " +
            "JOIN cs.car c " +
            "JOIN c.carModel cm " +
            "WHERE cs.progress = '판매중' " +
            "AND (c.distance >= :startDistance) " +
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
            "AND (:carTypes IS NULL OR cm.carType IN :carTypes) " +
            "AND cs.progress = '판매중'")
    List<CarSalesEntity> getAllRecommendByBrandsAndCarTypes(@Param("brands") List<String> brands,
                                                            @Param("carTypes") List<String> carTypes);
}