package com.autoever.carstore.car.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class DetailCarResponseDto {
      LocalDateTime created_at;
      int price;
      int discount_price;
      String progress;
      int agency_id;
      long carId;
      int view_count;
      int like_count;
      String car_number;
      String color;
      boolean cruise_control;
      int distance;
      boolean heated_seat;
      boolean hud;
      boolean line_out_warning;
      boolean navigation;
      boolean parking_distance_warning;
      boolean sunroof;
      boolean ventilated_seat;
      String brand;
      String car_type;
      int displacement;
      String fuel;
      double fuel_efficiency;
      String gear;
      String model_name;
      String model_year;
      List<String> carImages;
      List<String> fixedImages;
      List<SimilarCarResponseDto> recommendCars;
}
