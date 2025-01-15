package com.autoever.carstore.car.service;

import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;
import com.autoever.carstore.user.dto.response.IsHeartCarResponseDto;
import com.autoever.carstore.user.dto.response.RecommendCarResponseDto;
import com.autoever.carstore.user.dto.response.TransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.UserCarTransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.UserCountingResponseDto;

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
    public List<TransactionStatusResponseDto> viewTransaction(long userId, String progress);
    public List<UserCarTransactionStatusResponseDto> viewUserCarTransaction(long userId, String progress);
    public List<IsHeartCarResponseDto> viewIsHeartCar(long userId);
    public List<RecommendCarResponseDto> viewUserCarRecommend(long userId);
}