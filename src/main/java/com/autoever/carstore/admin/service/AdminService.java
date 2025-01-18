package com.autoever.carstore.admin.service;

import com.autoever.carstore.admin.dto.request.RegistrationRequestDto;
import com.autoever.carstore.admin.dto.response.AgencyDto;
import com.autoever.carstore.admin.dto.response.JudgeResponseDto;
import com.autoever.carstore.admin.dto.response.RegistrationResponseDto;
import com.autoever.carstore.agency.dao.AgencyRepository;
import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.car.dao.CarPurchaseRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.entity.CarPurchaseEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.user.dto.response.TransactionsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CarSalesRepository carSalesRepository;
    private final CarPurchaseRepository carPurchaseRepository;
    private final AgencyRepository agencyRepository;

//    public List<TransactionsResponseDto> getAllRecentTransactions() {
//        List<CarSalesEntity> carSalesEntities = carSalesRepository.findAll();
//        List<TransactionsResponseDto> results = new ArrayList<>();
//
//        for (CarSalesEntity carSaleEntity : carSalesEntities) {
//            if (carSaleEntity.getUser() == null) { // 방어 코드 추가
//
//                continue; // 해당 레코드 스킵
//            }
//
//
//            for (CarSalesEntity carSalesEntity : carSalesEntities) {
//                TransactionsResponseDto transactionsResponseDto = TransactionsResponseDto.builder()
//                        .car_sales_id(carSalesEntity.getCarSalesId())
//                        .sales_date(carSalesEntity.getUpdatedAt())
//                        .progress(carSalesEntity.getProgress())
//                        .brand(carSalesEntity.getCar().getCarModel().getBrand())
//                        .model_name(carSalesEntity.getCar().getCarModel().getModelName())
//                        .order_number(carSalesEntity.getOrderNumber())
//                        .price(carSalesEntity.getPrice())
//                        .userId(carSalesEntity.getUser().getUserId()) // 유저 ID 추가
//                        .userName(carSalesEntity.getUser().getNickname()) // 유저 이름 추가
//                        .build();
//                results.add(transactionsResponseDto);
//            }
//
//        }
//        return results;
//
//    }


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

    public List<RegistrationResponseDto> getRegistrationCarsByProgress(boolean isVisible) {

        return carSalesRepository.findByIsVisibleAndProgress(isVisible, "판매중").stream()
                .map(carSalesEntity -> RegistrationResponseDto.builder()
                        .isVisible(carSalesEntity.isVisible())
                        .carSalesId(carSalesEntity.getCarSalesId())
                        .carId(carSalesEntity.getCar().getCarId())
                        .distance(carSalesEntity.getCar().getDistance())
                        .navigation(carSalesEntity.getCar().isNavigation())
                        .hud(carSalesEntity.getCar().isHud())
                        .ventilatedSeat(carSalesEntity.getCar().isVentilatedSeat())
                        .cruiseControl(carSalesEntity.getCar().isCruiseControl())
                        .sunroof(carSalesEntity.getCar().isSunroof())
                        .parkingDistanceWarning(carSalesEntity.getCar().isParkingDistanceWarning())
                        .lineOutWarning(carSalesEntity.getCar().isLineOutWarning())
                        .carImage(carSalesEntity.getCar().getImages().get(0).getImageUrl())
                        .carBrand(carSalesEntity.getCar().getCarModel().getBrand())
                        .carModel(carSalesEntity.getCar().getCarModel().getModelName())
                        .build())
                .toList();
    }

    public List<AgencyDto> getAllAgencies() {
        List<AgencyEntity> agencyEntities = agencyRepository.findAll();
        return agencyEntities.stream()
                .map(agencyEntity -> AgencyDto.builder()
                        .id(agencyEntity.getAgencyId())
                        .name(agencyEntity.getAgencyName())
                    .build())
                .toList();

    }

    public boolean submitRegistration(RegistrationRequestDto requestDto) {
        CarSalesEntity entity = carSalesRepository.findById(requestDto.getCarSalesId()).orElse(null);
        AgencyEntity agencyEntity = agencyRepository.findById(requestDto.getAgencyId()).orElseThrow(() -> new IllegalArgumentException("Invalid agency ID"));


        if(entity != null){
            entity.updateAgency(agencyEntity);
            entity.updatePrice(requestDto.getPrice());
            entity.updateIsVisible(requestDto.isVisible()); // isVisible 설정
            carSalesRepository.save(entity);
            return true;
        }
        return false;
    }
}