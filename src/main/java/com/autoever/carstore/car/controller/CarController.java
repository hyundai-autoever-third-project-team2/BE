package com.autoever.carstore.car.controller;

import com.autoever.carstore.car.dto.request.CompareRequestDto;
import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.request.RegisterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;
import com.autoever.carstore.car.service.CarPurchaseService;
import com.autoever.carstore.car.service.CarService;
import com.autoever.carstore.s3.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private CarPurchaseService carPurchaseService;

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

    @GetMapping("/search")
    public ResponseEntity<List<SearchCarResponseDto>> searchCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String modelName) {

        List<SearchCarResponseDto> result = null;
        try {
            result = carService.searchCars(brand, modelName);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

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

    @PostMapping("/registration")
    public ResponseEntity<String> registerCar(
            @RequestBody RegisterCarRequestDto registerCarRequestDto
            ) {
        carPurchaseService.registerCar(5, registerCarRequestDto.getCar_number(), registerCarRequestDto.getComments(), registerCarRequestDto.getImages()); // 1을 동적으로 수정 필요
        return ResponseEntity.ok("Successfully registered!");
    }

}
