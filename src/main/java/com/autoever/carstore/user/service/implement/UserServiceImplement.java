package com.autoever.carstore.user.service.implement;

import com.autoever.carstore.car.dao.CarModelRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.entity.CarModelEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
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
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyColorRepository surveyColorRepository;
    private final SurveyCarModelRepository surveyCarModelRepository;
    private final CarModelRepository carModelRepository;
    private final CarSalesRepository carSalesRepository;
    private final RecommendRepository recommendRepository;

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

        UserEntity userEntity = userRepository.findById(userId).orElse(null);

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

        for(Long carModelId : carModelIds) {
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
        for(Integer colorId : colors) {
            survey_color.add(stringColor.get(colorId));
            SurveyColorEntity surveyColor = SurveyColorEntity.builder()
                    .color(colorId)
                    .survey(survey)
                    .build();
            surveyColorRepository.save(surveyColor);
        }

       List<CarSalesEntity> allCars = carSalesRepository.getAllRecommend(min_price, max_price, min_distance, max_distance, min_model_year, max_model_year, carModelIds, survey_color);
        Collections.shuffle(allCars);
        // 9개까지 선택
        List<CarSalesEntity> selectedCars = allCars.stream()
                .limit(9)  // 최대 9개 항목만 선택
                .collect(Collectors.toList());

        // RecommendEntity에 저장
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

    private long getCarSalesId(List<CarSalesEntity> selectedCars, int index) {
        if (index < selectedCars.size()) {
            return selectedCars.get(index).getCarSalesId();
        }
        return -1;
    }

}

