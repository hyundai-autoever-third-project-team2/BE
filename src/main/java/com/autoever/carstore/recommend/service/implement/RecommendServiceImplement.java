package com.autoever.carstore.recommend.service.implement;

import com.autoever.carstore.car.dao.CarSalesLikeRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.car.entity.CarSalesLikeEntity;
import com.autoever.carstore.recommend.dao.RecommendRepository;
import com.autoever.carstore.recommend.entity.RecommendEntity;
import com.autoever.carstore.recommend.service.RecommendService;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendServiceImplement implements RecommendService {
    private final RecommendRepository recommendRepository;
    private final UserRepository userRepository;
    private final CarSalesRepository carSalesRepository;
    private final CarSalesLikeRepository carSalesLikeRepository;

    @Override
    @Scheduled(cron = "0 0 0 * * MON")
    public void updateRecommendations() {
        List<Long> userIds = recommendRepository.findAllUserIds();

        for (Long userId : userIds) {
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null) continue;

            List<CarSalesLikeEntity> likeEntities = carSalesLikeRepository.findByUserId(userId);
            int count = likeEntities.size();

            if(count >= 5){
                RecommendEntity recommendEntity = recommendRepository.findByUserId(userId);
                List<CarSalesEntity> recommendedCars = getNewRecommendations(likeEntities);

                recommendEntity = updateRecommendEntity(recommendEntity, recommendedCars);
                recommendRepository.save(recommendEntity);
            }
        }
    }

    private List<CarSalesEntity> getNewRecommendations(List<CarSalesLikeEntity> likeEntities) {
        Set<String> uniqueBrands = new HashSet<>();
        Set<String> uniqueCarTypes = new HashSet<>();

        List<String> brands = new ArrayList<>();
        List<String> carTypes = new ArrayList<>();

        for (CarSalesLikeEntity likeEntity : likeEntities) {
            String brand = likeEntity.getCarSales().getCar().getCarModel().getBrand();
            String carType = likeEntity.getCarSales().getCar().getCarModel().getCarType();

            // 중복 제거를 위해 Set에 추가
            if (uniqueBrands.add(brand)) { // Set에 없는 값만 추가
                brands.add(brand);
            }
            if (uniqueCarTypes.add(carType)) {
                carTypes.add(carType);
            }
        }

        List<CarSalesEntity> allCars = carSalesRepository.getAllRecommendByBrandsAndCarTypes(brands, carTypes);
        Collections.shuffle(allCars);

        return allCars.stream()
                .limit(Math.min(9, allCars.size()))
                .collect(Collectors.toList());
    }

    private RecommendEntity updateRecommendEntity(RecommendEntity recommendEntity, List<CarSalesEntity> recommendedCars) {
        recommendEntity = RecommendEntity.builder()
                .recommendCar1Id(recommendedCars.get(0).getCarSalesId())
                .recommendCar2Id(recommendedCars.get(1).getCarSalesId())
                .recommendCar3Id(recommendedCars.get(2).getCarSalesId())
                .recommendCar4Id(recommendedCars.get(3).getCarSalesId())
                .recommendCar5Id(recommendedCars.get(4).getCarSalesId())
                .recommendCar6Id(recommendedCars.get(5).getCarSalesId())
                .recommendCar7Id(recommendedCars.get(6).getCarSalesId())
                .recommendCar8Id(recommendedCars.get(7).getCarSalesId())
                .recommendCar9Id(recommendedCars.get(8).getCarSalesId())
                .build();
        return recommendEntity;
    }
}

