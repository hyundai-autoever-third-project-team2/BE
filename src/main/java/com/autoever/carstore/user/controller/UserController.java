package com.autoever.carstore.user.controller;

import com.autoever.carstore.car.service.CarService;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.user.dto.request.SurveyRequestDto;
import com.autoever.carstore.user.dto.response.IsHeartCarResponseDto;
import com.autoever.carstore.user.dto.response.RecommendCarResponseDto;
import com.autoever.carstore.user.dto.response.TransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.UserCarTransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.UserCountingResponseDto;
import com.autoever.carstore.user.dto.request.UpdateNicknameRequestDto;
import com.autoever.carstore.user.dto.request.UpdateProfileRequestDto;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final CarService carService;
    private final SecurityUtil securityUtil;

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        userService.logoutUser();

        return ResponseEntity.ok("");
    }

    @PutMapping("/update/profileImage")
    public ResponseEntity<?> updateProfileImage(
            @RequestBody UpdateProfileRequestDto requestDto
            ) {

        userService.updateUserProfile(requestDto);
        return ResponseEntity.ok("");
    }

    @PutMapping("/update/nickname")
    public ResponseEntity<?> updateProfileNickname(
            @RequestBody UpdateNicknameRequestDto requestDto
    ) {
        userService.updateUserNickname(requestDto);

        return ResponseEntity.ok("");
    }

    //구매내역 조회
    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionStatusResponseDto>> transaction() {
        UserEntity user = securityUtil.getLoginUser();
        List<TransactionStatusResponseDto> result = carService.viewTransaction(user.getUserId());
        return ResponseEntity.ok(result);
    }

    //판매 내역 조회
    @GetMapping("/userCarTransaction")
    public ResponseEntity<List<UserCarTransactionStatusResponseDto>> userCarTransaction(
            @RequestParam String progress
    ){
        UserEntity user = securityUtil.getLoginUser();
        List<UserCarTransactionStatusResponseDto> result = carService.viewUserCarTransaction(user.getUserId());
        return ResponseEntity.ok(result);
    }

    //찜한 상품 조회
    @GetMapping("/isHeartCar")
    public ResponseEntity<List<IsHeartCarResponseDto>> isHeartCar(){
        UserEntity user = securityUtil.getLoginUser();
        List<IsHeartCarResponseDto> result = carService.viewIsHeartCar(user.getUserId());
        return ResponseEntity.ok(result);
    }

    //마이페이지 -> 구매, 판매, 찜 갯수 조회
    @GetMapping("/userCounting")
    public ResponseEntity<UserCountingResponseDto> userCounting(){
        UserEntity user = securityUtil.getLoginUser();
        UserCountingResponseDto result = userService.getUserCounting(user.getUserId());
        return ResponseEntity.ok(result);
    }

    //설문조사 제출 폼
    @PostMapping("/survey")
    public ResponseEntity<String> survey(
            @RequestBody SurveyRequestDto surveyRequestDto
    ){
        UserEntity user = securityUtil.getLoginUser();
        userService.submitSurvey(user.getUserId(), surveyRequestDto);
        return ResponseEntity.ok("Successfully submitted survey");
    }

    //사용자 기반 추천 목록 조회
    @GetMapping("/userRecommend")
    public ResponseEntity<List<RecommendCarResponseDto>> userRecommend(){
        UserEntity user = securityUtil.getLoginUser();
        List<RecommendCarResponseDto> result = carService.viewUserCarRecommend(user.getUserId());
        return ResponseEntity.ok(result);
    }
}
