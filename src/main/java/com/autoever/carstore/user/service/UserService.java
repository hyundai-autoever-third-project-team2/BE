package com.autoever.carstore.user.service;

import com.autoever.carstore.user.dto.request.SurveyRequestDto;

public interface UserService {
    public void submitSurvey(long userId, SurveyRequestDto surveyRequestDto);
}
