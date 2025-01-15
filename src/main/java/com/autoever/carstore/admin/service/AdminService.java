package com.autoever.carstore.admin.service;

import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserService userService;

    public void toggleUserStatus(Long userId, boolean isActive) {
        UserEntity user = userService.getUserById(userId); // 유저 조회

        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        user.updateIsActive(); // 상태 반전
        userRepository.save(user); // 저장
    }


}
