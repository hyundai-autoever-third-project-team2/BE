package com.autoever.carstore.car.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LatelyCarResponseDto {
    Long carId;
    String imageUrl;
    String brand; //car model
    String model_name;
    String model_year;
    int distance; //car
    int price; //car sales
    double discount_price = 0;
    int month_price;
    LocalDateTime create_date; //car sales
    int view_count; //car_sales_view
    boolean isLiked;
}
