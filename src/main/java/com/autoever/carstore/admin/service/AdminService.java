package com.autoever.carstore.admin.service;

import com.autoever.carstore.admin.dto.request.RegistrationRequestDto;
import com.autoever.carstore.admin.dto.response.AgencyDto;
import com.autoever.carstore.admin.dto.response.JudgeResponseDto;
import com.autoever.carstore.admin.dto.response.RegistrationResponseDto;
import com.autoever.carstore.agency.dao.AgencyRepository;
import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.car.dao.CarPurchaseImageRepository;
import com.autoever.carstore.car.dao.CarPurchaseRepository;
import com.autoever.carstore.car.dao.CarSalesLikeRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.entity.CarEntity;
import com.autoever.carstore.car.entity.CarPurchaseEntity;
import com.autoever.carstore.car.entity.CarPurchaseImageEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.fcm.service.FCMService;
import com.autoever.carstore.notification.dto.NotificationRequestDto;
import com.autoever.carstore.notification.service.NotificationService;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdminService {
    private final CarSalesRepository carSalesRepository;
    private final CarPurchaseRepository carPurchaseRepository;
    private final AgencyRepository agencyRepository;
    private final CarPurchaseImageRepository carPurchaseImageRepository;
    private final FCMService fcmService;
    private final NotificationService notificationService;
    private final CarSalesLikeRepository carSalesLikeRepository;

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

    @Transactional
    public void completeJudge(Long purchaseId, int price) {
        CarPurchaseEntity car = carPurchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid purchase ID"));
        car.updatePrice(price);
        car.updateProgress("심사 완료");

        carPurchaseRepository.save(car);

        String title = "등록 차량 심사 결과";
        String body = String.format("""
[TABOLKA] 고객님의 차량 매입가가 산정되었습니다!

소중한 %s (%s)의
매입 견적이 완료되었습니다.

제안 매입가: %,d 만원

지금 바로 앱에서 확인해보세요 👉
""", car.getCar().getCarModel().getModelName(), car.getCar().getCarNumber(), price);

        NotificationRequestDto notification = NotificationRequestDto.builder()
                .user(car.getUser())
                .notificationType(0)
                .title(title)
                .content(body)
                .build();

        try{
            fcmService.sendMessageTo(car.getUser().getFcmToken(), title, body);
            notificationService.addNotification(notification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void rejectJudge(Long purchaseId) {
        CarPurchaseEntity car = carPurchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid purchase ID"));
        car.updateProgress("거절");
        carPurchaseRepository.save(car);

        String title = "등록 차량 심사 결과";
        String body = String.format("""
[TABOLKA] 고객님의 차량 매입이 거부되었습니다!

고객님의 차량 %s (%s)을 점검해본 결과 매입이 어려운 것으로 판단되었습니다.

지금 바로 앱에서 확인해보세요 👉
""", car.getCar().getCarModel().getModelName(), car.getCar().getCarNumber());

        NotificationRequestDto notification = NotificationRequestDto.builder()
                .user(car.getUser())
                .notificationType(0)
                .title(title)
                .content(body)
                .build();

        try{
            fcmService.sendMessageTo(car.getUser().getFcmToken(), title, body);
            notificationService.addNotification(notification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Page<RegistrationResponseDto> getRegistrationCarsByProgress(boolean isVisible, Pageable pageable) {

        if(isVisible == false) {
            return carPurchaseRepository.findByProgress("판매중", pageable)
                    .map(carPurchaseEntity -> RegistrationResponseDto.builder()
                            .carPurchaseId(carPurchaseEntity.getCarPurchaseId())
                            .carId(carPurchaseEntity.getCar().getCarId())
                            .distance(carPurchaseEntity.getCar().getDistance())
                            .navigation(carPurchaseEntity.getCar().isNavigation())
                            .hud(carPurchaseEntity.getCar().isHud())
                            .ventilatedSeat(carPurchaseEntity.getCar().isVentilatedSeat())
                            .heatedSeat(carPurchaseEntity.getCar().isHeatedSeat())
                            .cruiseControl(carPurchaseEntity.getCar().isCruiseControl())
                            .sunroof(carPurchaseEntity.getCar().isSunroof())
                            .parkingDistanceWarning(carPurchaseEntity.getCar().isParkingDistanceWarning())
                            .lineOutWarning(carPurchaseEntity.getCar().isLineOutWarning())
                            .carImage(carPurchaseEntity.getCar().getImages().get(0).getImageUrl())
                            .carBrand(carPurchaseEntity.getCar().getCarModel().getBrand())
                            .carModel(carPurchaseEntity.getCar().getCarModel().getModelName())
                            .build());
        }else{
            return carSalesRepository.findByProgress("판매중", pageable)
                    .map(carSalesEntity ->  RegistrationResponseDto.builder()
                            .carPurchaseId(carSalesEntity.getCarSalesId())
                            .carId(carSalesEntity.getCar().getCarId())
                            .distance(carSalesEntity.getCar().getDistance())
                            .navigation(carSalesEntity.getCar().isNavigation())
                            .hud(carSalesEntity.getCar().isHud())
                            .ventilatedSeat(carSalesEntity.getCar().isVentilatedSeat())
                            .heatedSeat(carSalesEntity.getCar().isHeatedSeat())
                            .cruiseControl(carSalesEntity.getCar().isCruiseControl())
                            .sunroof(carSalesEntity.getCar().isSunroof())
                            .parkingDistanceWarning(carSalesEntity.getCar().isParkingDistanceWarning())
                            .lineOutWarning(carSalesEntity.getCar().isLineOutWarning())
                            .carImage(carSalesEntity.getCar().getImages().get(0).getImageUrl())
                            .carBrand(carSalesEntity.getCar().getCarModel().getBrand())
                            .carModel(carSalesEntity.getCar().getCarModel().getModelName())
                            .build());
        }
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

    @Transactional
    public void submitRegistration(RegistrationRequestDto requestDto) {
        CarPurchaseEntity entity = carPurchaseRepository.findById(requestDto.getCarPurchaseId()).orElse(null);
        AgencyEntity agencyEntity = agencyRepository.findById(requestDto.getAgencyId()).orElseThrow(() -> new IllegalArgumentException("Invalid agency ID"));

        CarSalesEntity carSalesEntity = CarSalesEntity.builder()
                .car(entity.getCar())
                .user(null)
                .agency(agencyEntity)
                .price(requestDto.getPrice())
                .discountPrice(0)
                .progress("판매중")
                .salesDate(null)
                .orderNumber(null)
                .isVisible(true)
                .count(0)
                .build();
        carSalesRepository.save(carSalesEntity);
        carPurchaseRepository.delete(entity);

//        if(entity != null){


//            List<UserEntity> users = carSalesLikeRepository.findUsersByCarModelId(entity.getCar().getCarModel());
//
//            for(UserEntity user : users){
//                String title = "관심 차종 등록";
//                String body = String.format("""
//[TABOLKA] 신규 매물 알림!
//
//고객님이 관심 있으신 %s %s 차량이 새로 등록되었습니다.
//
//가격 : %,d 만원
//
//지금 바로 앱에서 확인해보세요 👉
//""", entity.getCar().getCarModel().getModelName(), entity.getCar().getCarModel().getModelYear(), entity.getPrice());
//
//                NotificationRequestDto notification = NotificationRequestDto.builder()
//                        .user(user)
//                        .notificationType(1)
//                        .title(title)
//                        .content(body)
//                        .build();
//
//                try{
//                    fcmService.sendMessageTo(user.getFcmToken(), title, body);
//                    notificationService.addNotification(notification);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }

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