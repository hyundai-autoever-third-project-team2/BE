package com.autoever.carstore.car.service;

import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;
import com.autoever.carstore.user.dto.response.IsHeartCarResponseDto;
import com.autoever.carstore.user.dto.response.RecommendCarResponseDto;
import com.autoever.carstore.user.dto.response.TransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.UserCarTransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.UserCountingResponseDto;
import com.autoever.carstore.user.entity.UserEntity;

import java.util.List;

public interface CarService{
    public List<LatelyCarResponseDto> getLatelyCarList(long userId);
    public List<DomesticCarResponseDto> getDomesticCarList(long userId);
    public List<AbroadCarResponseDto> getAbroadCarList(long userId);
    public List<PopularityCarResponseDto> getPopularityCarList(long userId);
    public List<DiscountCarResponseDto> getDiscountCarList(long userId);
    public List<LikelyCarResponseDto> getLikelyCarList(long userId);
    public List<SearchCarResponseDto> searchCars(String brand, String modelName, long userId);
    public List<FilterCarResponseDto> filterCars(FilterCarRequestDto requestDto, long userId);
    public DetailCarResponseDto findByCarId(Long carId, Long userId);
    public List<DetailCarResponseDto> compareCars(List<Long> carIds, long userId);
    public void updateViewCount(Long carId);
    public List<TransactionStatusResponseDto> viewTransaction(long userId);
    public List<UserCarTransactionStatusResponseDto> viewUserCarTransaction(long userId);
    public List<IsHeartCarResponseDto> viewIsHeartCar(long userId);
    public List<RecommendCarResponseDto> viewUserCarRecommend(long userId);
    public void contractCar(long userId, long carId);
    public List<SearchCarResponseDto> searchCarsBrandAndModelName(String token, String token1, long userId);
    public void updateDiscountPrice();
    public void isLikedCar(UserEntity user, long carId);
    public void unLikedCar(UserEntity user, long carId);
}