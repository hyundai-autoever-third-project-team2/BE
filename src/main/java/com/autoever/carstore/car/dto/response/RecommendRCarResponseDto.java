package com.autoever.carstore.car.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class RecommendRCarResponseDto{
    long carId;
    String imageUrl;
    String brand; //car model
    String model_name;
    int price; //car sales
    double discount_price = 0;
}
