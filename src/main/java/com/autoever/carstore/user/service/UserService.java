package com.autoever.carstore.user.service;

import com.autoever.carstore.user.dto.request.SurveyRequestDto;
import com.autoever.carstore.user.dto.request.UpdateNicknameRequestDto;
import com.autoever.carstore.user.dto.request.UpdateProfileRequestDto;
import com.autoever.carstore.user.dto.response.UserCountingResponseDto;
import com.autoever.carstore.user.entity.UserEntity;

import java.util.List;
import com.autoever.carstore.user.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserCountingResponseDto getUserCounting(long userId);

    void submitSurvey(long userId, SurveyRequestDto surveyRequestDto);

    UserEntity getUserById(Long id);

    String getUserName();

    Page<UserEntity> getAllUsers(Pageable pageable);

    void toggleUserActive(Long id);

    void logoutUser();

    void updateUserNickname(UpdateNicknameRequestDto request);

    void updateUserProfile(UpdateProfileRequestDto request);

    UserResponseDto getUserInfo(String email);

    void updateFcmToken(String token);
}
