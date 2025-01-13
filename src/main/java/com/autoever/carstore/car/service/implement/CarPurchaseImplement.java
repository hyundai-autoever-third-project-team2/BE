package com.autoever.carstore.car.service.implement;

import com.autoever.carstore.car.dao.CarPurchaseImageRepository;
import com.autoever.carstore.car.dao.CarPurchaseRepository;
import com.autoever.carstore.car.dao.CarRepository;
import com.autoever.carstore.car.entity.CarEntity;
import com.autoever.carstore.car.entity.CarPurchaseEntity;
import com.autoever.carstore.car.entity.CarPurchaseImageEntity;
import com.autoever.carstore.car.service.CarPurchaseService;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarPurchaseImplement implements CarPurchaseService {
    private final CarPurchaseRepository carPurchaseRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarPurchaseImageRepository carPurchaseImageRepository;

    @Override
    public void registerCar(long userId, String car_number, String comments, List<String> imageUrls) {
        CarEntity carEntity = carRepository.findByCarNumber(car_number);
        UserEntity userEntity = userRepository.findByUserId(userId);
        CarPurchaseEntity carPurchaseEntity = CarPurchaseEntity.builder()
                .comments(comments)
                .price(0)
                .progress("심사전")
                .purchaseDate(LocalDateTime.now())
                .car(carEntity)
                .user(userEntity)
                .build();
        CarPurchaseEntity carPurchase = carPurchaseRepository.save(carPurchaseEntity);
        for (String imageUrl : imageUrls) {
            CarPurchaseImageEntity carPurchaseImageEntity = CarPurchaseImageEntity.builder()
                    .carPurchase(carPurchase)
                    .imageUrl(imageUrl)
                    .build();
            carPurchaseImageRepository.save(carPurchaseImageEntity);
        }

        return;
    }

}
