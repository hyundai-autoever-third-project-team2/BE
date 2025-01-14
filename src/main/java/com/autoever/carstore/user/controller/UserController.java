package com.autoever.carstore.user.controller;

import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.car.service.CarService;
import com.autoever.carstore.user.dto.response.TransactionStatusResponseDto;
import com.autoever.carstore.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
