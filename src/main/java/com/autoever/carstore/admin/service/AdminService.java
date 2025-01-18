package com.autoever.carstore.admin.service;

import com.autoever.carstore.admin.dto.response.JudgeResponseDto;
import com.autoever.carstore.car.dao.CarPurchaseRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.entity.CarPurchaseEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.dto.response.TransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.TransactionsResponseDto;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CarSalesRepository carSalesRepository;
    private final CarPurchaseRepository carPurchaseRepository;

    public List<TransactionsResponseDto> getAllRecentTransactions() {
        List<CarSalesEntity> carSalesEntities = carSalesRepository.findAll();
        List<TransactionsResponseDto> results = new ArrayList<>();

        for (CarSalesEntity carSaleEntity : carSalesEntities) {
            if (carSaleEntity.getUser() == null) { // 방어 코드 추가

                continue; // 해당 레코드 스킵
            }


            for (CarSalesEntity carSalesEntity : carSalesEntities) {
                TransactionsResponseDto transactionsResponseDto = TransactionsResponseDto.builder()
                        .car_sales_id(carSalesEntity.getCarSalesId())
                        .sales_date(carSalesEntity.getUpdatedAt())
                        .progress(carSalesEntity.getProgress())
                        .brand(carSalesEntity.getCar().getCarModel().getBrand())
                        .model_name(carSalesEntity.getCar().getCarModel().getModelName())
                        .order_number(carSalesEntity.getOrderNumber())
                        .price(carSalesEntity.getPrice())
                        .userId(carSalesEntity.getUser().getUserId()) // 유저 ID 추가
                        .userName(carSalesEntity.getUser().getNickname()) // 유저 이름 추가
                        .build();
                results.add(transactionsResponseDto);
            }

        }
        return results;

    }


    public List<JudgeResponseDto> getCarsByProgress(String progress) {
        switch (progress) {
            case "before":
                progress = "심사중";
                break;
            case "deny":
                progress = "거절";
                break;
            case "complete":
                progress = "심사 완료";
                break;
            default:
                progress = "심사중";
                break;
        }
        return carPurchaseRepository.findByProgress(progress).stream()
                .map(carPurchaseEntity -> JudgeResponseDto.builder()
                        .userId(carPurchaseEntity.getUser().getUserId())
                        .userName(carPurchaseEntity.getUser().getNickname())
                        .carPurchaseId(carPurchaseEntity.getCarPurchaseId())
                        .carImage(carPurchaseEntity.getCar().getImages().get(0).getImageUrl())
                        .userCarImages(carPurchaseEntity.getImages())
                        .purchaseDate(carPurchaseEntity.getPurchaseDate())
                        .progress(carPurchaseEntity.getProgress())
                        .brand(carPurchaseEntity.getCar().getCarModel().getBrand())
                        .modelName(carPurchaseEntity.getCar().getCarModel().getModelName())
                        .comments(carPurchaseEntity.getComments())
                        .build())
                .toList();
    }

    public void completeJudge(Long purchaseId, int price) {
        CarPurchaseEntity car = carPurchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid purchase ID"));
        car.updatePrice(price);
        car.updateProgress("심사 완료");
        carPurchaseRepository.save(car);
    }

    public void rejectJudge(Long purchaseId) {
        CarPurchaseEntity car = carPurchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid purchase ID"));
        car.updateProgress("거절");
        carPurchaseRepository.save(car);
    }
}