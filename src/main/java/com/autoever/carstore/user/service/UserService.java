package com.autoever.carstore.user.service;

import com.autoever.carstore.user.dto.request.SurveyRequestDto;
import com.autoever.carstore.user.dto.response.UserResponseDto;

public interface UserService {
    public void submitSurvey(long userId, SurveyRequestDto surveyRequestDto);

    UserResponseDto getUserInfo(String email);
}
