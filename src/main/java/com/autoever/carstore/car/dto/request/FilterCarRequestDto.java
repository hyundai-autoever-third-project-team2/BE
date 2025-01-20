package com.autoever.carstore.car.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterCarRequestDto {
    List<String> carTypes;
    int start_year;
    int end_year;
    int start_distance;
    int end_distance;
    int start_price;
    int end_price;
    List<String> colors;
}
