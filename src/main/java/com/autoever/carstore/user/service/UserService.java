package com.autoever.carstore.user.service;

import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.user.dto.request.UpdateNicknameRequestDto;
import com.autoever.carstore.user.dto.request.UpdateProfileRequestDto;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.dao.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String getUserName() {
        UserEntity user = securityUtil.getLoginUser();

        return user.getNickname();
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public void toggleUserActive(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.updateIsActive();
    }

    public void logoutUser() {
        // Refresh Token 삭제 (DB나 메모리에서 제거)
        UserEntity user = securityUtil.getLoginUser();

        user.deleteToken();
    }

    @Transactional
    public void updateUserNickname(UpdateNicknameRequestDto request) {
        UserEntity user = securityUtil.getLoginUser();

        user.updateNickname(request.getNickname());

    }

    @Transactional
    public void updateUserProfile(UpdateProfileRequestDto request) {
        UserEntity user = securityUtil.getLoginUser();

        user.updateProfileImage(request.getProfileImage());

    }
}
