package com.autoever.carstore.user.service;

import com.autoever.carstore.user.dto.response.UserCountingResponseDto;

public interface UserService {
    public UserCountingResponseDto getUserCounting(long userId);
}
