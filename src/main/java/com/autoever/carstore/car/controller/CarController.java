package com.autoever.carstore.car.controller;

import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;
import com.autoever.carstore.car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;

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

}
