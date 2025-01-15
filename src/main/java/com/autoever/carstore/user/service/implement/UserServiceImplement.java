package com.autoever.carstore.user.service.implement;

import com.autoever.carstore.car.dao.CarPurchaseRepository;
import com.autoever.carstore.car.dao.CarSalesLikeRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.dto.response.UserCountingResponseDto;
import com.autoever.carstore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final CarPurchaseRepository carPurchaseRepository;
    private final CarSalesRepository carSalesRepository;
    private final CarSalesLikeRepository carSalesLikeRepository;

    @Override
    public UserCountingResponseDto getUserCounting(long userId) {
        int purchaseCount = carPurchaseRepository.countByUserId(userId);
        int saleCount = carSalesRepository.countByUserId(userId);
        int heartCount = carSalesLikeRepository.countByUserId(userId);

        UserCountingResponseDto userCountingResponseDto = UserCountingResponseDto.builder()
                .purchaseCount(purchaseCount)
                .saleCount(saleCount)
                .heartCount(heartCount)
                .build();
        return userCountingResponseDto;
    }
}
