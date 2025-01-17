package com.autoever.carstore.car.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class SimilarCarResponseDto {
    long carId;
    String imageUrl;
    String brand;
    String model_name;
    int price; //car sales
    double discount_price = 0;
    boolean isLiked;
}
