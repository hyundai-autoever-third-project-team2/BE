package com.autoever.carstore.car.service.implement;

import com.autoever.carstore.car.dao.CarModelRepository;
import com.autoever.carstore.car.dao.CarRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.dao.CarSalesViewRepository;
import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;
import com.autoever.carstore.car.entity.CarImageEntity;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.car.entity.FixedImageEntity;
import com.autoever.carstore.car.service.CarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImplement implements CarService {
    private final CarSalesRepository carSalesRepository;
    private final CarModelRepository carModelRepository;
    private final CarRepository carRepository;
    private final CarSalesViewRepository carSalesViewRepository;

    @Override
    public List<LatelyCarResponseDto> getLatelyCarList() {
        List<LatelyCarResponseDto> result = new ArrayList<>();

        List<CarSalesEntity> car_sales_lately_list = carSalesRepository.findAllLately();

        for(CarSalesEntity car_sales: car_sales_lately_list) {
            LatelyCarResponseDto lately_car;
            Long carId = car_sales.getCar().getCarId();
            String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
            String brand = car_sales.getCar().getCarModel().getBrand();
            String model_name = car_sales.getCar().getCarModel().getModelName();
            String model_year = car_sales.getCar().getCarModel().getModelYear();
            int distance = car_sales.getCar().getDistance();

            int price = car_sales.getPrice();
            int discount_price = 0;
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            if (create_date.isBefore(oneWeekAgo)) {
                discount_price = (int) (price * 0.97); // 3% 할인
                month_price = discount_price / 6;
            }

            int view_count = carSalesViewRepository.getCountByCarSalesId(car_sales.getCarSalesId());

            lately_car = LatelyCarResponseDto.builder()
                    .carId(carId)
                    .imageUrl(imageUrl)
                    .brand(brand)
                    .model_name(model_name)
                    .model_year(model_year)
                    .distance(distance)
                    .price(price)
                    .discount_price(discount_price)
                    .month_price(month_price)
                    .create_date(create_date)
                    .view_count(view_count)
                    .build();

            result.add(lately_car);
        }

        return result.isEmpty() ? null : result;
    }

    @Override
    public List<DomesticCarResponseDto> getDomesticCarList(){
        List<DomesticCarResponseDto> result = new ArrayList<>();
        List<String> brands = Arrays.asList("현대", "기아", "제네시스", "KG모빌리티");
        List<CarSalesEntity> car_sales_domestic_list = carSalesRepository.findAllWithCarAndCarModelByBrands(brands);

        for(CarSalesEntity car_sales: car_sales_domestic_list) {
            DomesticCarResponseDto domestic_car;
            long carId = car_sales.getCar().getCarId();
            String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
            String brand = car_sales.getCar().getCarModel().getBrand();
            String model_name = car_sales.getCar().getCarModel().getModelName();
            String model_year = car_sales.getCar().getCarModel().getModelYear();
            int distance = car_sales.getCar().getDistance();

            int price = car_sales.getPrice();
            int discount_price = 0;
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            if (create_date.isBefore(oneWeekAgo)) {
                discount_price = (int) (price * 0.97); // 3% 할인
                month_price = discount_price / 6;
            }

            int view_count = car_sales.getCount();

            domestic_car = DomesticCarResponseDto.builder()
                    .carId(carId)
                    .imageUrl(imageUrl)
                    .brand(brand)
                    .model_name(model_name)
                    .model_year(model_year)
                    .distance(distance)
                    .price(price)
                    .discount_price(discount_price)
                    .month_price(month_price)
                    .create_date(create_date)
                    .view_count(view_count)
                    .build();

            result.add(domestic_car);
        }

        return result.isEmpty() ? null : result;
    }

    @Override
    public List<AbroadCarResponseDto> getAbroadCarList() {
        List<AbroadCarResponseDto> result = new ArrayList<>();

        List<String> brands = Arrays.asList("현대", "기아", "제네시스", "KG모빌리티");

        List<CarSalesEntity> car_sales_abroad_list = carSalesRepository.findAllWithCarAndCarModelExcludingBrands(brands);

        for (CarSalesEntity car_sales : car_sales_abroad_list) {
            AbroadCarResponseDto abroad_car;
            long carId = car_sales.getCar().getCarId();
            String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
            String brand = car_sales.getCar().getCarModel().getBrand();
            String model_name = car_sales.getCar().getCarModel().getModelName();
            String model_year = car_sales.getCar().getCarModel().getModelYear();
            int distance = car_sales.getCar().getDistance();

            int price = car_sales.getPrice();
            int discount_price = 0;
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            if (create_date.isBefore(oneWeekAgo)) {
                discount_price = (int) (price * 0.97); // 3% 할인
                month_price = discount_price / 6;
            }

            int view_count = car_sales.getCount();

            abroad_car = AbroadCarResponseDto.builder()
                    .carId(carId)
                    .imageUrl(imageUrl)
                    .brand(brand)
                    .model_name(model_name)
                    .model_year(model_year)
                    .distance(distance)
                    .price(price)
                    .discount_price(discount_price)
                    .month_price(month_price)
                    .create_date(create_date)
                    .view_count(view_count)
                    .build();

            result.add(abroad_car);
        }
        return result.isEmpty() ? null : result;
    }

    @Override
    public List<PopularityCarResponseDto> getPopularityCarList() {
        List<PopularityCarResponseDto> result = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 50); // 0번 페이지에서 50개 가져오기
        List<CarSalesEntity> car_sales_popularity_list = carSalesRepository.findTop50ByOrderByLikesDesc(pageable);

        for (CarSalesEntity car_sales : car_sales_popularity_list) {
            PopularityCarResponseDto popularity_car;
            long carId = car_sales.getCar().getCarId();
            String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
            String brand = car_sales.getCar().getCarModel().getBrand();
            String model_name = car_sales.getCar().getCarModel().getModelName();
            String model_year = car_sales.getCar().getCarModel().getModelYear();
            int distance = car_sales.getCar().getDistance();

            int price = car_sales.getPrice();
            int discount_price = 0;
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            if (create_date.isBefore(oneWeekAgo)) {
                discount_price = (int) (price * 0.97); // 3% 할인
                month_price = discount_price / 6;
            }

            int view_count = car_sales.getCount();

            popularity_car = PopularityCarResponseDto.builder()
                    .carId(carId)
                    .imageUrl(imageUrl)
                    .brand(brand)
                    .model_name(model_name)
                    .model_year(model_year)
                    .distance(distance)
                    .price(price)
                    .discount_price(discount_price)
                    .month_price(month_price)
                    .create_date(create_date)
                    .view_count(view_count)
                    .build();

            result.add(popularity_car);
        }
        return result.isEmpty() ? null : result;
    }

    @Override
    public List<DiscountCarResponseDto> getDiscountCarList() {
        List<DiscountCarResponseDto> result = new ArrayList<>();

        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(1, ChronoUnit.WEEKS);
        List<CarSalesEntity> car_sales_discount_list = carSalesRepository.findAllOlderThanAWeek(oneWeekAgo);

        for (CarSalesEntity car_sales : car_sales_discount_list) {
            DiscountCarResponseDto discount_car;
            long carId = car_sales.getCar().getCarId();
            String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
            String brand = car_sales.getCar().getCarModel().getBrand();
            String model_name = car_sales.getCar().getCarModel().getModelName();
            String model_year = car_sales.getCar().getCarModel().getModelYear();
            int distance = car_sales.getCar().getDistance();

            int price = car_sales.getPrice();
            double discount_price = (price * 0.97);
            int month_price = (int)discount_price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = car_sales.getCount();

            discount_car = DiscountCarResponseDto.builder()
                    .carId(carId)
                    .imageUrl(imageUrl)
                    .brand(brand)
                    .model_name(model_name)
                    .model_year(model_year)
                    .distance(distance)
                    .price(price)
                    .discount_price(discount_price)
                    .month_price(month_price)
                    .create_date(create_date)
                    .view_count(view_count)
                    .build();

            result.add(discount_car);
        }
        return result.isEmpty() ? null : result;
    }

    @Override
    public List<LikelyCarResponseDto> getLikelyCarList() {
        List<LikelyCarResponseDto> result = new ArrayList<>();

        List<CarSalesEntity> car_sales_likely_list = carSalesRepository.findByOrderByLikesDesc();

        for (CarSalesEntity car_sales : car_sales_likely_list) {
            LikelyCarResponseDto likely_car;
            long carId = car_sales.getCar().getCarId();
            String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
            String brand = car_sales.getCar().getCarModel().getBrand();
            String model_name = car_sales.getCar().getCarModel().getModelName();
            String model_year = car_sales.getCar().getCarModel().getModelYear();
            int distance = car_sales.getCar().getDistance();
            int price = car_sales.getPrice();
            int discount_price = 0;
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            if (create_date.isBefore(oneWeekAgo)) {
                discount_price = (int) (price * 0.97); // 3% 할인
                month_price = discount_price / 6;
            }

            int view_count = car_sales.getCount();

            likely_car = LikelyCarResponseDto.builder()
                    .carId(carId)
                    .imageUrl(imageUrl)
                    .brand(brand)
                    .model_name(model_name)
                    .model_year(model_year)
                    .distance(distance)
                    .price(price)
                    .discount_price(discount_price)
                    .month_price(month_price)
                    .create_date(create_date)
                    .view_count(view_count)
                    .build();

            result.add(likely_car);
        }
        return result.isEmpty() ? null : result;
    }

    @Override
    public List<SearchCarResponseDto> searchCars(String brand, String modelName) {
        List<SearchCarResponseDto> result = new ArrayList<>();
        List<CarSalesEntity> car_sales_search_list = carSalesRepository.findByBrandAndCarName(brand, modelName);

        for(CarSalesEntity car_sales : car_sales_search_list) {
                SearchCarResponseDto search_car;
                long carId = car_sales.getCar().getCarId();
                String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
                String s_brand = car_sales.getCar().getCarModel().getBrand();
                String model_name = car_sales.getCar().getCarModel().getModelName();
                String model_year = car_sales.getCar().getCarModel().getModelYear();
                int distance = car_sales.getCar().getDistance();
                int price = car_sales.getPrice();
                int discount_price = 0;
                int month_price = price / 6;

                LocalDateTime create_date = car_sales.getCreatedAt();

                int view_count = car_sales.getCount();
                LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
                if (create_date.isBefore(oneWeekAgo)) {
                    discount_price = (int) (price * 0.97); // 3% 할인
                    month_price = discount_price / 6;
                }

                search_car = SearchCarResponseDto.builder()
                        .carId(carId)
                        .imageUrl(imageUrl)
                        .brand(s_brand)
                        .model_name(model_name)
                        .model_year(model_year)
                        .distance(distance)
                        .price(price)
                        .discount_price(discount_price)
                        .month_price(month_price)
                        .create_date(create_date)
                        .view_count(view_count)
                        .build();

                result.add(search_car);
            }
            return result.isEmpty() ? null : result;
        }

    @Override
    public List<FilterCarResponseDto> filterCars(FilterCarRequestDto requestDto) {
        List<FilterCarResponseDto> result = new ArrayList<>();
        List<String> carTypes = requestDto.getCarTypes();
        int startDisplacement = requestDto.getStart_displacement();
        int endDisplacement = requestDto.getEnd_displacement();
        int startDistance = requestDto.getStart_distance();
        int endDistance = requestDto.getEnd_distance();
        int startPrice = requestDto.getStart_price();
        int endPrice = requestDto.getEnd_price();

        List<String> colors = requestDto.getColors();

        List<CarSalesEntity> car_sales_filter_list = carSalesRepository.filterCars(carTypes, startDisplacement, endDisplacement, startPrice, endPrice, colors);

        for(CarSalesEntity car_sales : car_sales_filter_list) {
            FilterCarResponseDto filter_car;
            long carId = car_sales.getCar().getCarId();
            String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
            String s_brand = car_sales.getCar().getCarModel().getBrand();
            String model_name = car_sales.getCar().getCarModel().getModelName();
            String model_year = car_sales.getCar().getCarModel().getModelYear();
            int distance = car_sales.getCar().getDistance();
            int price = car_sales.getPrice();
            int discount_price = 0;
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = car_sales.getCount();
            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            if (create_date.isBefore(oneWeekAgo)) {
                discount_price = (int) (price * 0.97); // 3% 할인
                month_price = discount_price / 6;
            }

            filter_car = FilterCarResponseDto.builder()
                    .carId(carId)
                    .imageUrl(imageUrl)
                    .brand(s_brand)
                    .model_name(model_name)
                    .model_year(model_year)
                    .distance(distance)
                    .price(price)
                    .discount_price(discount_price)
                    .month_price(month_price)
                    .create_date(create_date)
                    .view_count(view_count)
                    .build();

            result.add(filter_car);
        }
        return result.isEmpty() ? null : result;

    }

    @Override
    public DetailCarResponseDto findByCarId(Long carId) {
        CarSalesEntity carSales = carSalesRepository.findByCarId(carId);
        LocalDateTime create_date = carSales.getCreatedAt();
        int price = carSales.getPrice();
        int discount_price = 0;
        int month_price = price / 6;
        String progress = carSales.getProgress();
        int agency_id = carSales.getAgency().getAgencyId();
        long response_carId = carSales.getCar().getCarId();
        int view_count = carSales.getCount();
        int like_count = carSales.getTotalLikes();
        String car_number = carSales.getCar().getCarNumber();
        String color = carSales.getCar().getColor();
        boolean cruise_control = carSales.getCar().isCruiseControl();
        int distance = carSales.getCar().getDistance();
        boolean heated_seat = carSales.getCar().isHeatedSeat();
        boolean hud = carSales.getCar().isHud();
        boolean line_out_warning = carSales.getCar().isLineOutWarning();
        boolean navigation = carSales.getCar().isNavigation();
        boolean parking_distance_warning = carSales.getCar().isParkingDistanceWarning();
        boolean sunroof = carSales.getCar().isSunroof();
        boolean ventilated_seat = carSales.getCar().isVentilatedSeat();
        String brand = carSales.getCar().getCarModel().getBrand();
        String car_type = carSales.getCar().getCarModel().getCarType();
        int displacement = carSales.getCar().getCarModel().getDisplacement();
        String fuel = carSales.getCar().getCarModel().getFuel();
        double fuel_efficiency = carSales.getCar().getCarModel().getFuelEfficiency();
        String gear = carSales.getCar().getCarModel().getGear();
        String model_name = carSales.getCar().getCarModel().getModelName();
        String model_year = carSales.getCar().getCarModel().getModelYear();


        List<String> carImages = new ArrayList<>();
        for(CarImageEntity carImageEntity : carSales.getCar().getImages()){
            carImages.add(carImageEntity.getImageUrl());
        }
        List<String> fixedCarImages = new ArrayList<>();
        for(FixedImageEntity fixedImageEntity : carSales.getCar().getFixedImages()){
            fixedCarImages.add(fixedImageEntity.getFixedImageUrl());
        }

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        if (create_date.isBefore(oneWeekAgo)) {
            discount_price = (int) (price * 0.97); // 3% 할인
            month_price = discount_price / 6;
        }

        //유사 차량 3개 만들기
        List<RecommendRCarResponseDto> recommendRCarResponseDtos = new ArrayList<>();
        List <CarSalesEntity> recommendsEntity = carSalesRepository.findSimilarCar(car_type, brand);

        int count = 0;
        for (CarSalesEntity carSalesEntity : recommendsEntity) {
            if (count >= 3) break;
            int recommend_discount_price = 0;
            LocalDateTime recommend_create_date = carSalesEntity.getCreatedAt();

            LocalDateTime recommend_oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            if (recommend_create_date.isBefore(recommend_oneWeekAgo)) {
                recommend_discount_price = (int) (carSalesEntity.getPrice() * 0.97); // 3% 할인
            }

            RecommendRCarResponseDto recommendDto = RecommendRCarResponseDto.builder()
                    .carId(carSalesEntity.getCar().getCarId())
                    .imageUrl(carSalesEntity.getCar().getImages().get(0).getImageUrl())
                    .brand(carSalesEntity.getCar().getCarModel().getBrand())
                    .model_name(carSalesEntity.getCar().getCarModel().getModelName())
                    .price(carSalesEntity.getPrice())
                    .discount_price(recommend_discount_price)
                    .build();
            recommendRCarResponseDtos.add(recommendDto); // 리스트에 추가
            count++;
        }



        DetailCarResponseDto result = DetailCarResponseDto.builder()
                .created_at(carSales.getCreatedAt())
                .price(price)
                .discount_price(discount_price)
                .progress(progress)
                .agency_id(agency_id)
                .carId(response_carId)
                .view_count(view_count)
                .like_count(like_count)
                .car_number(car_number)
                .color(color)
                .cruise_control(cruise_control)
                .distance(distance)
                .heated_seat(heated_seat)
                .hud(hud)
                .line_out_warning(line_out_warning)
                .navigation(navigation)
                .parking_distance_warning(parking_distance_warning)
                .sunroof(sunroof)
                .ventilated_seat(ventilated_seat)
                .brand(brand)
                .car_type(car_type)
                .displacement(displacement)
                .fuel(fuel)
                .fuel_efficiency(fuel_efficiency)
                .gear(gear)
                .model_name(model_name)
                .model_year(model_year)
                .carImages(carImages)
                .fixedImages(fixedCarImages)
                .recommendRCars(recommendRCarResponseDtos)
                .build();

        return result;
    }

    @Override
    public List<DetailCarResponseDto> compareCars(List<Long> carIds) {
        List<DetailCarResponseDto> result = new ArrayList<>();
        for(Long carId : carIds){
            CarSalesEntity carSales = carSalesRepository.findByCarId(carId);
            LocalDateTime create_date = carSales.getCreatedAt();
            int price = carSales.getPrice();
            int discount_price = 0;
            int month_price = price / 6;
            String progress = carSales.getProgress();
            int agency_id = carSales.getAgency().getAgencyId();
            long response_carId = carSales.getCar().getCarId();
            int view_count = carSales.getCount();
            int like_count = carSales.getTotalLikes();
            String car_number = carSales.getCar().getCarNumber();
            String color = carSales.getCar().getColor();
            boolean cruise_control = carSales.getCar().isCruiseControl();
            int distance = carSales.getCar().getDistance();
            boolean heated_seat = carSales.getCar().isHeatedSeat();
            boolean hud = carSales.getCar().isHud();
            boolean line_out_warning = carSales.getCar().isLineOutWarning();
            boolean navigation = carSales.getCar().isNavigation();
            boolean parking_distance_warning = carSales.getCar().isParkingDistanceWarning();
            boolean sunroof = carSales.getCar().isSunroof();
            boolean ventilated_seat = carSales.getCar().isVentilatedSeat();
            String brand = carSales.getCar().getCarModel().getBrand();
            String car_type = carSales.getCar().getCarModel().getCarType();
            int displacement = carSales.getCar().getCarModel().getDisplacement();
            String fuel = carSales.getCar().getCarModel().getFuel();
            double fuel_efficiency = carSales.getCar().getCarModel().getFuelEfficiency();
            String gear = carSales.getCar().getCarModel().getGear();
            String model_name = carSales.getCar().getCarModel().getModelName();
            String model_year = carSales.getCar().getCarModel().getModelYear();
            List<String> carImages = new ArrayList<>();
            for(CarImageEntity carImageEntity : carSales.getCar().getImages()){
                carImages.add(carImageEntity.getImageUrl());
            }
            List<String> fixedCarImages = new ArrayList<>();
            for(FixedImageEntity fixedImageEntity : carSales.getCar().getFixedImages()){
                fixedCarImages.add(fixedImageEntity.getFixedImageUrl());
            }

            LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
            if (create_date.isBefore(oneWeekAgo)) {
                discount_price = (int) (price * 0.97); // 3% 할인
                month_price = discount_price / 6;
            }

            DetailCarResponseDto dto = DetailCarResponseDto.builder()
                    .created_at(carSales.getCreatedAt())
                    .price(price)
                    .discount_price(discount_price)
                    .progress(progress)
                    .agency_id(agency_id)
                    .carId(response_carId)
                    .view_count(view_count)
                    .like_count(like_count)
                    .car_number(car_number)
                    .color(color)
                    .cruise_control(cruise_control)
                    .distance(distance)
                    .heated_seat(heated_seat)
                    .hud(hud)
                    .line_out_warning(line_out_warning)
                    .navigation(navigation)
                    .parking_distance_warning(parking_distance_warning)
                    .sunroof(sunroof)
                    .ventilated_seat(ventilated_seat)
                    .brand(brand)
                    .car_type(car_type)
                    .displacement(displacement)
                    .fuel(fuel)
                    .fuel_efficiency(fuel_efficiency)
                    .gear(gear)
                    .model_name(model_name)
                    .model_year(model_year)
                    .carImages(carImages)
                    .fixedImages(fixedCarImages)
                    .build();
            result.add(dto);
        }
        return result;
    }

    @Override
    public void updateViewCount(Long carId) {
        // 차 정보를 DB에서 조회
        Optional<CarSalesEntity> carSalesEntity = Optional.ofNullable(carSalesRepository.findByCarId(carId));

        if (carSalesEntity.isPresent()) {
            int count = carSalesEntity.get().getCount()+ 1;
            carSalesEntity.get().setCount(count);
            carSalesRepository.save(carSalesEntity.get());
        } else {
            // 차가 존재하지 않으면 처리 (예: 예외 처리)
            throw new EntityNotFoundException("Car with ID " + carId + " not found.");
        }
    }


}
