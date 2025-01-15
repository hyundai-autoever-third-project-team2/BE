package com.autoever.carstore.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyRequestDto {
    int min_price;
    int max_price;
    int min_distance;
    int max_distance;
    int min_model_year;
    int max_model_year;
    List<Long> car_model_ids;
    List<Integer> colors;
}
