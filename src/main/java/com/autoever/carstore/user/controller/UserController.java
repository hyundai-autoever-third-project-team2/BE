package com.autoever.carstore.user.controller;

import com.autoever.carstore.jwt.JWTUtil;
import com.autoever.carstore.user.dto.request.UpdateNicknameRequestDto;
import com.autoever.carstore.user.dto.request.UpdateProfileRequestDto;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout() {

        userService.logoutUser();

        return ResponseEntity.ok("");
    }

    @PutMapping("/user/update/profileImage")
    public ResponseEntity<?> updateProfileImage(
            @RequestBody UpdateProfileRequestDto requestDto
            ) {

        userService.updateUserProfile(requestDto);
        return ResponseEntity.ok("");
    }

    @PutMapping("/user/update/nickname")
    public ResponseEntity<?> updateProfileNickname(
            @RequestBody UpdateNicknameRequestDto requestDto
    ) {
        userService.updateUserNickname(requestDto);

        return ResponseEntity.ok("");
    }
}
