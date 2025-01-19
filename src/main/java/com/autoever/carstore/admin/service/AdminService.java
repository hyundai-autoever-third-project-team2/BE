package com.autoever.carstore.admin.service;

import com.autoever.carstore.admin.dto.request.RegistrationRequestDto;
import com.autoever.carstore.admin.dto.response.AgencyDto;
import com.autoever.carstore.admin.dto.response.JudgeResponseDto;
import com.autoever.carstore.admin.dto.response.RegistrationResponseDto;
import com.autoever.carstore.agency.dao.AgencyRepository;
import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.car.dao.CarPurchaseImageRepository;
import com.autoever.carstore.car.dao.CarPurchaseRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.entity.CarPurchaseEntity;
import com.autoever.carstore.car.entity.CarPurchaseImageEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.user.dto.response.TransactionsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CarSalesRepository carSalesRepository;
    private final CarPurchaseRepository carPurchaseRepository;
    private final AgencyRepository agencyRepository;
    private final CarPurchaseImageRepository carPurchaseImageRepository;

    public Page<JudgeResponseDto> getCarsByProgress(String progress, Pageable pageable) {
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

        return carPurchaseRepository.findByProgress(progress, pageable)
                .map(carPurchaseEntity -> JudgeResponseDto.builder()
                        .userId(carPurchaseEntity.getUser().getUserId())
                        .userName(carPurchaseEntity.getUser().getNickname())
                        .carPurchaseId(carPurchaseEntity.getCarPurchaseId())
                        .carImage(carPurchaseEntity.getCar().getImages().get(0).getImageUrl())
                        .purchaseDate(carPurchaseEntity.getPurchaseDate())
                        .progress(carPurchaseEntity.getProgress())
                        .brand(carPurchaseEntity.getCar().getCarModel().getBrand())
                        .modelName(carPurchaseEntity.getCar().getCarModel().getModelName())
                        .comments(carPurchaseEntity.getComments())
                        .build()
                );
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

    public Page<RegistrationResponseDto> getRegistrationCarsByProgress(boolean isVisible, Pageable pageable) {

        return carSalesRepository.findByIsVisibleAndProgress(isVisible, "판매중", pageable)
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
                        .build());
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

    public List<String> getAllUserCarImages() {
        // 모든 CarPurchaseImageEntity를 가져옴
        List<CarPurchaseImageEntity> carPurchaseImageEntities = carPurchaseImageRepository.findAll();

        // 이미지 URL만 추출하여 리스트로 변환
        return carPurchaseImageEntities.stream()
                .map(CarPurchaseImageEntity::getImageUrl) // 각 엔티티의 imageUrl 필드를 가져옴
                .collect(Collectors.toList()); // 리스트로 변환하여 반환
    }

    public List<String> getImagesByPurchaseId(Long purchaseId) {
        return carPurchaseImageRepository.findByCarPurchase_CarPurchaseId(purchaseId)
                .stream()
                .map(CarPurchaseImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }
}