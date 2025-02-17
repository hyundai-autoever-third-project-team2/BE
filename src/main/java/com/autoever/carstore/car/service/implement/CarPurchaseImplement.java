package com.autoever.carstore.car.service.implement;

import com.autoever.carstore.car.dao.CarPurchaseImageRepository;
import com.autoever.carstore.car.dao.CarPurchaseRepository;
import com.autoever.carstore.car.dao.CarRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.entity.CarEntity;
import com.autoever.carstore.car.entity.CarPurchaseEntity;
import com.autoever.carstore.car.entity.CarPurchaseImageEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.car.service.CarPurchaseService;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarPurchaseImplement implements CarPurchaseService {
    private final CarPurchaseRepository carPurchaseRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarPurchaseImageRepository carPurchaseImageRepository;
    private final CarSalesRepository carSalesRepository;

    @Override
    public void registerCar(long userId, String car_number, String comments, List<String> imageUrls) {
        CarEntity carEntity = carRepository.findByCarNumber(car_number);
        UserEntity userEntity = userRepository.findByUserId(userId);
        CarPurchaseEntity carPurchaseEntity = CarPurchaseEntity.builder()
                .comments(comments)
                .price(0)
                .progress("심사중")
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

    @Override
    public void updateCar(long carPurchaseId, String progress) {
        CarPurchaseEntity carPurchaseEntity = carPurchaseRepository.findByCarPurchaseId(carPurchaseId);
        CarEntity carEntity = carPurchaseEntity.getCar();
        UserEntity userEntity = carPurchaseEntity.getUser();
        String comments = carPurchaseEntity.getComments();
        int price = carPurchaseEntity.getPrice();
        LocalDateTime purchaseDate = carPurchaseEntity.getPurchaseDate();
        boolean isDeleted = carPurchaseEntity.isDeleted();
        List<CarPurchaseImageEntity> images = carPurchaseEntity.getImages();

        carPurchaseEntity = CarPurchaseEntity.builder()
                .carPurchaseId(carPurchaseId)
                .car(carEntity)
                .user(userEntity)
                .comments(comments)
                .progress(progress)
                .price(price)
                .purchaseDate(purchaseDate)
                .isDeleted(isDeleted)
                .images(images)
                .build();

        carPurchaseRepository.save(carPurchaseEntity);
    }

}
