package com.autoever.carstore.user.controller;

import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.car.service.CarService;
import com.autoever.carstore.user.dto.request.SurveyRequestDto;
import com.autoever.carstore.user.dto.response.IsHeartCarResponseDto;
import com.autoever.carstore.user.dto.response.TransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.UserCarTransactionStatusResponseDto;
import com.autoever.carstore.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;

    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionStatusResponseDto>> transaction(
            @RequestParam String progress
    ) {
        long userId = 5;
        List<TransactionStatusResponseDto> result = carService.viewTransaction(userId, progress);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/userCarTransaction")
    public ResponseEntity<List<UserCarTransactionStatusResponseDto>> userCarTransaction(
            @RequestParam String progress
    ){
        long userId = 5;
        List<UserCarTransactionStatusResponseDto> result = carService.viewUserCarTransaction(userId, progress);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/isHeartCar")
    public ResponseEntity<List<IsHeartCarResponseDto>> isHeartCar(){
        long userId = 5;
        List<IsHeartCarResponseDto> result = carService.viewIsHeartCar(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/survey")
    public ResponseEntity<String> survey(
            @RequestBody SurveyRequestDto surveyRequestDto
    ){
        long userId = 5;
        userService.submitSurvey(userId, surveyRequestDto);
        return ResponseEntity.ok("Successfully submitted survey");
    }
}
