package com.autoever.carstore.car.controller;

import com.autoever.carstore.car.dto.request.CompareRequestDto;
import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.request.RegisterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;
import com.autoever.carstore.car.service.CarPurchaseService;
import com.autoever.carstore.car.service.CarService;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
import com.autoever.carstore.s3.ImageUploadService;
import com.autoever.carstore.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.StringTokenizer;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {
    private final SecurityUtil securityUtil;
    @Autowired
    private CarService carService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private CarPurchaseService carPurchaseService;

    //국내 차량 조회
    @GetMapping("/domestic")
    public ResponseEntity<? super List<DomesticCarResponseDto>> viewDomesticCar() {
        List<DomesticCarResponseDto> result = null;

        try {
            result = carService.getDomesticCarList();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    //해외 차량 조회
    @GetMapping("/abroad")
    public ResponseEntity<? super List<AbroadCarResponseDto>> viewAbroadCar() {
        List<AbroadCarResponseDto> result = null;

        try {
            result = carService.getAbroadCarList();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    //인기 차량 조회 top50
    @GetMapping("/popularity")
    public ResponseEntity<? super List<PopularityCarResponseDto>> viewPopularityCar(){
        List<PopularityCarResponseDto> result = null;

        try {
            result = carService.getPopularityCarList();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    //할인중인 차량 조회
    @GetMapping("/discount")
    public ResponseEntity<?super List<DiscountCarResponseDto>> viewDiscountCar(){
        List<DiscountCarResponseDto> result = null;

        try {
            result = carService.getDiscountCarList();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    //최신순 차량 조회
    @GetMapping("/lately")
    public ResponseEntity<? super List<LatelyCarResponseDto>> viewLatelyCar(){
        List<LatelyCarResponseDto> result = null;

        try {
            result = carService.getLatelyCarList();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    //인기순 차량 조회(전체보기에서의 필터링)
    @GetMapping("/likely")
    public ResponseEntity<? super List<LikelyCarResponseDto>> viewLikelyCar(){
        List<LikelyCarResponseDto> result = null;

        try {
            result = carService.getLikelyCarList();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    //검색 차량 조회(브랜드, 모델)
    @GetMapping("/search")
    public ResponseEntity<List<SearchCarResponseDto>> searchCars(
            @RequestParam String searchCar) {

        List<SearchCarResponseDto> result = null;
        try {
            String[] tokens = searchCar.split(" ", 2);
            if (tokens.length == 1) {
                result = carService.searchCars(searchCar, searchCar);
            } else if (tokens.length == 2) {
                result = carService.searchCarsBrandAndModelName(tokens[0], tokens[1]);
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    //카테고리 필터링
    @GetMapping("/filter")
    public ResponseEntity<List<FilterCarResponseDto>> filterCars(
            @RequestBody FilterCarRequestDto filterCarRequestDto
    ) {

        List<FilterCarResponseDto> result = null;
        try {
            result = carService.filterCars(filterCarRequestDto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    //차량 상세보기
    @GetMapping("/detail")
    public ResponseEntity<DetailCarResponseDto> detailCars(
        @RequestParam(required = false) Long carId
    ){
        DetailCarResponseDto result = null;
        try {
            carService.updateViewCount(carId);
            result = carService.findByCarId(carId);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    //차량 비교하기
    @GetMapping("/compares")
    public ResponseEntity<List<DetailCarResponseDto>> compareCars(
            @RequestBody CompareRequestDto compareRequestDto
    ){
        List<DetailCarResponseDto> result = null;
        try {
            result = carService.compareCars(compareRequestDto.getCarIds());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    //차량 등록하기
    @PostMapping("/registration")
    public ResponseEntity<String> registerCar(
            @RequestBody RegisterCarRequestDto registerCarRequestDto
            ) {
        UserEntity user = securityUtil.getLoginUser();
        carPurchaseService.registerCar(user.getUserId(), registerCarRequestDto.getCar_number(), registerCarRequestDto.getComments(), registerCarRequestDto.getImages());
        return ResponseEntity.ok("Successfully registered!");
    }

    //등록 차량 취소하기
    @PutMapping("/updatePurchaseCar")
    public ResponseEntity<String> updatePurchaseCar(
            @RequestParam long car_purchase_id,
            @RequestParam String progress
    ){
        carPurchaseService.updateCar(car_purchase_id, progress);
        return ResponseEntity.ok("Successfully updated!");
    }


    //차량 거래하기
    @PutMapping("/contract")
    public ResponseEntity<String> contractCar(
            @RequestParam long carId
    ){
        UserEntity user = securityUtil.getLoginUser();
        carService.contractCar(user.getUserId(), carId);
        return ResponseEntity.ok("Successfully contract!");
    }

}
