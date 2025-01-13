package com.autoever.carstore.car.service.implement;

import com.autoever.carstore.car.dao.CarModelRepository;
import com.autoever.carstore.car.dao.CarRepository;
import com.autoever.carstore.car.dao.CarSalesRepository;
import com.autoever.carstore.car.dao.CarSalesViewRepository;
import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;
import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.car.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

}
