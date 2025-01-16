package com.autoever.carstore.car.service;

import com.autoever.carstore.car.entity.CarPurchaseEntity;

import java.util.List;

public interface CarPurchaseService {
    public void registerCar(long userId, String car_number, String comments, List<String> imageUrls);
    public void cancleCar(long carPurchaseId, String progress);
}
