package com.autoever.carstore.car.service;

import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;

import java.util.List;

public interface CarService{
    public List<LatelyCarResponseDto> getLatelyCarList();
    public List<DomesticCarResponseDto> getDomesticCarList();
    public List<AbroadCarResponseDto> getAbroadCarList();
    public List<PopularityCarResponseDto> getPopularityCarList();
    public List<DiscountCarResponseDto> getDiscountCarList();
    public List<LikelyCarResponseDto> getLikelyCarList();
    public List<SearchCarResponseDto> searchCars(String brand, String modelName);
    public List<FilterCarResponseDto> filterCars(FilterCarRequestDto requestDto);
    public DetailCarResponseDto findByCarId(Long carId);
    public List<DetailCarResponseDto> compareCars(List<Long> carIds);
    public void updateViewCount(Long carId);
}