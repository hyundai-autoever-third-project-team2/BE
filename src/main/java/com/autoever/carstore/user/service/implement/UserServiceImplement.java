package com.autoever.carstore.user.service.implement;

import com.autoever.carstore.car.dao.CarModelRepository;
import com.autoever.carstore.car.entity.CarModelEntity;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyColorRepository surveyColorRepository;
    private final SurveyCarModelRepository surveyCarModelRepository;
    private final CarModelRepository carModelRepository;

    @Override
    public void submitSurvey(long userId, SurveyRequestDto surveyRequestDto) {
        int min_price = surveyRequestDto.getMin_price();
        int max_price = surveyRequestDto.getMax_price();
        int min_distance = surveyRequestDto.getMin_distance();
        int max_distance = surveyRequestDto.getMax_distance();
        int min_model_year = surveyRequestDto.getMin_model_year();
        int max_model_year = surveyRequestDto.getMax_model_year();
        List<Integer> carModelIds = surveyRequestDto.getCar_model_ids();
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

        for(Integer carModelId : carModelIds) {
            CarModelEntity carModel = carModelRepository.findByCarModelId(carModelId);
            SurveyCarModelEntity surveyCarModelEntity = SurveyCarModelEntity.builder()
                    .carModel(carModel)
                    .survey(survey)
                    .build();
            surveyCarModelRepository.save(surveyCarModelEntity);
        }

        for(Integer colorId : colors) {
            SurveyColorEntity surveyColor = SurveyColorEntity.builder()
                    .color(colorId)
                    .survey(survey)
                    .build();
            surveyColorRepository.save(surveyColor);
        }

    }
}
