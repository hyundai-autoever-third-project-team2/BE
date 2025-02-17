package com.autoever.carstore.user.service.implement;

import com.autoever.carstore.car.dao.CarModelRepository;
import com.autoever.carstore.car.dao.CarPurchaseRepository;
import com.autoever.carstore.car.dao.CarSalesLikeRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.entity.CarModelEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.recommend.dao.RecommendRepository;
import com.autoever.carstore.recommend.entity.RecommendEntity;
import com.autoever.carstore.survey.dao.SurveyCarModelRepository;
import com.autoever.carstore.survey.dao.SurveyColorRepository;
import com.autoever.carstore.survey.dao.SurveyRepository;
import com.autoever.carstore.survey.entity.SurveyCarModelEntity;
import com.autoever.carstore.survey.entity.SurveyColorEntity;
import com.autoever.carstore.survey.entity.SurveyEntity;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.dto.request.SurveyRequestDto;
import com.autoever.carstore.user.dto.request.UpdateNicknameRequestDto;
import com.autoever.carstore.user.dto.request.UpdateProfileRequestDto;
import com.autoever.carstore.user.dto.response.UserCountingResponseDto;
import com.autoever.carstore.user.dto.response.UserResponseDto;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final CarPurchaseRepository carPurchaseRepository;
    private final CarSalesRepository carSalesRepository;
    private final CarSalesLikeRepository carSalesLikeRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyColorRepository surveyColorRepository;
    private final SurveyCarModelRepository surveyCarModelRepository;
    private final CarModelRepository carModelRepository;
    private final RecommendRepository recommendRepository;
    private final SecurityUtil securityUtil;

    @Override
    public void submitSurvey(long userId, SurveyRequestDto surveyRequestDto) {
        int min_price = surveyRequestDto.getMin_price();
        int max_price = surveyRequestDto.getMax_price();
        int min_distance = surveyRequestDto.getMin_distance();
        int max_distance = surveyRequestDto.getMax_distance();
        int min_model_year = surveyRequestDto.getMin_model_year();
        int max_model_year = surveyRequestDto.getMax_model_year();
        List<Long> carModelIds = surveyRequestDto.getCar_model_ids();
        List<Integer> colors = surveyRequestDto.getColors();

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        SurveyEntity surveyEntity = SurveyEntity.builder()
                .minPrice(min_price)
                .maxPrice(max_price)
                .minDistance(min_distance)
                .maxDistance(max_distance)
                .minModelYear(min_model_year)
                .maxModelYear(max_model_year)
                .user(userEntity)
                .build();
        SurveyEntity survey = surveyRepository.save(surveyEntity);

        userEntity.updateSurvey();
        userRepository.save(userEntity);

        for (Long carModelId : carModelIds) {
            CarModelEntity carModel = carModelRepository.findByCarModelId(carModelId);
            SurveyCarModelEntity surveyCarModelEntity = SurveyCarModelEntity.builder()
                    .carModel(carModel)
                    .survey(survey)
                    .build();
            surveyCarModelRepository.save(surveyCarModelEntity);
        }

        // 색상 목록
        List<String> stringColor = List.of("갈색", "검정", "기타", "남색", "녹색", "은색", "진주", "파랑", "하늘", "회색", "흰색");
        List<String> survey_color = new ArrayList<>();
        for (Integer colorId : colors) {
            survey_color.add(stringColor.get(colorId));
            SurveyColorEntity surveyColor = SurveyColorEntity.builder()
                    .color(colorId)
                    .survey(survey)
                    .build();
            surveyColorRepository.save(surveyColor);
        }

        List<CarSalesEntity> allCars = carSalesRepository.getAllRecommend(min_price, max_price, min_distance, max_distance, min_model_year, max_model_year, carModelIds, survey_color);
        List<CarSalesEntity> subCars = carSalesRepository.findAll();
        Collections.shuffle(allCars);
        Collections.shuffle(subCars);

        Set<Long> selectedCarIds = new HashSet<>();
        List<CarSalesEntity> selectedCars = new ArrayList<>();

        for (CarSalesEntity car : allCars) {
            if (selectedCars.size() >= 9) break;
            if (!selectedCarIds.contains(car.getCarSalesId())) {
                selectedCars.add(car);
                selectedCarIds.add(car.getCarSalesId());
            }
        }
        for (CarSalesEntity car : subCars) {
            if (selectedCars.size() >= 9) break;
            if (!selectedCarIds.contains(car.getCarSalesId())) {
                selectedCars.add(car);
                selectedCarIds.add(car.getCarSalesId());
            }
        }

        RecommendEntity recommendEntity = RecommendEntity.builder()
                .recommendCar1Id(getCarSalesId(selectedCars, 0))
                .recommendCar2Id(getCarSalesId(selectedCars, 1))
                .recommendCar3Id(getCarSalesId(selectedCars, 2))
                .recommendCar4Id(getCarSalesId(selectedCars, 3))
                .recommendCar5Id(getCarSalesId(selectedCars, 4))
                .recommendCar6Id(getCarSalesId(selectedCars, 5))
                .recommendCar7Id(getCarSalesId(selectedCars, 6))
                .recommendCar8Id(getCarSalesId(selectedCars, 7))
                .recommendCar9Id(getCarSalesId(selectedCars, 8))
                .user(userEntity)
                .build();

// 추천 저장소에 저장
        recommendRepository.save(recommendEntity);
    }

    @Override
    public UserCountingResponseDto getUserCounting(long userId) {
        int purchaseCount = carPurchaseRepository.countByUserId(userId);
        int saleCount = carSalesRepository.countByUserId(userId);
        int heartCount = carSalesLikeRepository.countByUserId(userId);

        return UserCountingResponseDto.builder()
                .purchaseCount(purchaseCount)
                .saleCount(saleCount)
                .heartCount(heartCount)
                .build();
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public String getUserName() {
        UserEntity user = securityUtil.getLoginUser();
        return user.getNickname();
    }

    @Override
    public Page<UserEntity> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void toggleUserActive(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.updateIsActive();
    }

    @Override
    public void logoutUser() {
        UserEntity user = securityUtil.getLoginUser();
        user.deleteToken();
    }

    @Override
    @Transactional
    public void updateUserNickname(UpdateNicknameRequestDto request) {
        UserEntity user = securityUtil.getLoginUser();
        user.updateNickname(request.getNickname());
    }

    @Override
    @Transactional
    public void updateUserProfile(UpdateProfileRequestDto request) {
        UserEntity user = securityUtil.getLoginUser();
        user.updateProfileImage(request.getProfileImage());
    }

    @Override
    public UserResponseDto getUserInfo(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponseDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }

    @Override
    public void updateFcmToken(String token) {
        UserEntity user = securityUtil.getLoginUser();
        user.updateFcmToken(token);

        userRepository.save(user);
    }

    private Long getCarSalesId(List<CarSalesEntity> selectedCars, int index) {
        if (index < selectedCars.size()) {
            return selectedCars.get(index).getCarSalesId();
        }
        return null;
    }
}