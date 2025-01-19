package com.autoever.carstore.admin.dto.response;


import com.autoever.carstore.car.entity.CarPurchaseImageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JudgeResponseDto {
    Long userId;
    String userName;
    long carPurchaseId;
    // 자동차 모델 이미지
    String carImage;
    LocalDateTime purchaseDate;
    String progress;
    String brand;
    String modelName;
    String comments;
}
