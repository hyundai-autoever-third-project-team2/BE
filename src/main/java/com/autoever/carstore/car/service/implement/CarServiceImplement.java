package com.autoever.carstore.car.service.implement;

import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.car.dao.*;
import com.autoever.carstore.car.dto.request.FilterCarRequestDto;
import com.autoever.carstore.car.dto.response.*;
import com.autoever.carstore.car.entity.*;
import com.autoever.carstore.car.service.CarService;
import com.autoever.carstore.fcm.service.FCMService;
import com.autoever.carstore.notification.dto.NotificationRequestDto;
import com.autoever.carstore.notification.service.NotificationService;
import com.autoever.carstore.recommend.dao.RecommendRepository;
import com.autoever.carstore.recommend.entity.RecommendEntity;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.dto.response.IsHeartCarResponseDto;
import com.autoever.carstore.user.dto.response.RecommendCarResponseDto;
import com.autoever.carstore.user.dto.response.TransactionStatusResponseDto;
import com.autoever.carstore.user.dto.response.UserCarTransactionStatusResponseDto;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class CarServiceImplement implements CarService {
    private final CarSalesRepository carSalesRepository;
    private final CarModelRepository carModelRepository;
    private final CarRepository carRepository;
    private final CarSalesViewRepository carSalesViewRepository;
    private final CarPurchaseRepository carPurchaseRepository;
    private final CarSalesLikeRepository carSalesLikeRepository;
    private final RecommendRepository recommendRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FCMService fcmService;
    private final NotificationService notificationService;

    //최신순 차량 조회 서비스
    @Override
    public List<LatelyCarResponseDto> getLatelyCarList(long userId) {
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
            int discount_price = car_sales.getDiscountPrice();
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = carSalesViewRepository.getCountByCarSalesId(car_sales.getCarSalesId());
            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                    .isLiked(isLiked)
                    .build();

            result.add(lately_car);
        }

        return result.isEmpty() ? null : result;
    }

    //국내차량 조회 서비스
    @Override
    public List<DomesticCarResponseDto> getDomesticCarList(long userId){
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
            int discount_price = car_sales.getDiscountPrice();
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = car_sales.getCount();
            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                    .isLiked(isLiked)
                    .build();

            result.add(domestic_car);
        }

        return result.isEmpty() ? null : result;
    }

    //해외 차량 조회 서비스
    @Override
    public List<AbroadCarResponseDto> getAbroadCarList(long userId) {

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
            int discount_price = car_sales.getDiscountPrice();
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = car_sales.getCount();

            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                    .isLiked(isLiked)
                    .build();

            result.add(abroad_car);
        }
        return result.isEmpty() ? null : result;
    }

    //인기 차량 조회 서비스(top50)
    @Override
    public List<PopularityCarResponseDto> getPopularityCarList(long userId) {
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
            int discount_price = car_sales.getDiscountPrice();
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = car_sales.getCount();
            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                    .isLiked(isLiked)
                    .build();

            result.add(popularity_car);
        }
        return result.isEmpty() ? null : result;
    }

    //할인 차량 조회 서비스
    @Override
    public List<DiscountCarResponseDto> getDiscountCarList(long userId) {
        List<DiscountCarResponseDto> result = new ArrayList<>();
        List<CarSalesEntity> car_sales_discount_list = carSalesRepository.findAll();

        for (CarSalesEntity car_sales : car_sales_discount_list) {
            if(car_sales.getDiscountPrice() > 0){
                DiscountCarResponseDto discount_car;
                long carId = car_sales.getCar().getCarId();
                String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
                String brand = car_sales.getCar().getCarModel().getBrand();
                String model_name = car_sales.getCar().getCarModel().getModelName();
                String model_year = car_sales.getCar().getCarModel().getModelYear();
                int distance = car_sales.getCar().getDistance();

                int price = car_sales.getPrice();
                int discount_price = car_sales.getDiscountPrice();
                int month_price = price / 6;

                LocalDateTime create_date = car_sales.getCreatedAt();

                int view_count = car_sales.getCount();
                boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                        .isLiked(isLiked)
                        .build();

                result.add(discount_car);
            }
        }
        return result.isEmpty() ? null : result;
    }

    //인기순 차량 조회(전체보기에서의 필터링) 서비스
    @Override
    public List<LikelyCarResponseDto> getLikelyCarList(long userId) {
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
            int discount_price = car_sales.getDiscountPrice();
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = car_sales.getCount();

            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                    .isLiked(isLiked)
                    .build();

            result.add(likely_car);
        }
        return result.isEmpty() ? null : result;
    }

    //검색 차량 조회(브랜드, 모델) 서비스
    @Override
    public List<SearchCarResponseDto> searchCars(String brand, String modelName, long userId) {
        List<SearchCarResponseDto> result = new ArrayList<>();
        List<CarSalesEntity> car_sales_search_list = carSalesRepository.findByBrandOrCarName(brand, modelName);

        for(CarSalesEntity car_sales : car_sales_search_list) {
                SearchCarResponseDto search_car;
                long carId = car_sales.getCar().getCarId();
                String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
                String s_brand = car_sales.getCar().getCarModel().getBrand();
                String model_name = car_sales.getCar().getCarModel().getModelName();
                String model_year = car_sales.getCar().getCarModel().getModelYear();
                int distance = car_sales.getCar().getDistance();
                int price = car_sales.getPrice();
                int discount_price = car_sales.getDiscountPrice();
                int month_price = price / 6;

                LocalDateTime create_date = car_sales.getCreatedAt();

                int view_count = car_sales.getCount();

                boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                        .isLiked(isLiked)
                        .build();

                result.add(search_car);
            }
            return result.isEmpty() ? null : result;
        }

    //카테고리 필터링 서비스
    @Override
    public List<FilterCarResponseDto> filterCars(FilterCarRequestDto requestDto, long userId) {
        List<FilterCarResponseDto> result = new ArrayList<>();
        List<String> carTypes = requestDto.getCarTypes();
        int startDisplacement = requestDto.getStart_displacement();
        int endDisplacement = requestDto.getEnd_displacement();
        int startDistance = requestDto.getStart_distance();
        int endDistance = requestDto.getEnd_distance();
        int startPrice = requestDto.getStart_price();
        int endPrice = requestDto.getEnd_price();

        List<String> colors = requestDto.getColors();

        List<CarSalesEntity> car_sales_filter_list = carSalesRepository.filterCars(carTypes, startDisplacement, endDisplacement, startDistance, endDistance, startPrice, endPrice, colors);

        for(CarSalesEntity car_sales : car_sales_filter_list) {
            FilterCarResponseDto filter_car;
            long carId = car_sales.getCar().getCarId();
            String imageUrl = car_sales.getCar().getImages().get(0).getImageUrl();
            String s_brand = car_sales.getCar().getCarModel().getBrand();
            String model_name = car_sales.getCar().getCarModel().getModelName();
            String model_year = car_sales.getCar().getCarModel().getModelYear();
            int distance = car_sales.getCar().getDistance();
            int price = car_sales.getPrice();
            int discount_price = car_sales.getDiscountPrice();
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = car_sales.getCount();
            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                    .isLiked(isLiked)
                    .build();

            result.add(filter_car);
        }
        return result.isEmpty() ? null : result;

    }

    //차량 상세보기 서비스
    @Override
    public DetailCarResponseDto findByCarId(Long carId, Long userId) {
        CarSalesEntity carSales = carSalesRepository.findByCarId(carId);
        LocalDateTime create_date = carSales.getCreatedAt();
        int price = carSales.getPrice();
        int discount_price = carSales.getDiscountPrice();
        int month_price = price / 6;
        String progress = carSales.getProgress();
        int agency_id = carSales.getAgency().getAgencyId();
        String agency_name = carSales.getAgency().getAgencyName();
        BigDecimal latitude = carSales.getAgency().getLatitude();
        BigDecimal longitude = carSales.getAgency().getLongitude();
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
        boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(carSales.getCarSalesId(), userId);

        List<String> carImages = new ArrayList<>();
        for(CarImageEntity carImageEntity : carSales.getCar().getImages()){
            carImages.add(carImageEntity.getImageUrl());
        }

        List<String> fixedCarImages = new ArrayList<>();
        for(FixedImageEntity fixedImageEntity : carSales.getCar().getFixedImages()){
            fixedCarImages.add(fixedImageEntity.getFixedImageUrl());
        }

        //유사 차량 3개 만들기
        List<SimilarCarResponseDto> similarCarResponseDtos = new ArrayList<>();
        List <CarSalesEntity> recommendsEntity = carSalesRepository.findSimilarCar(car_type, brand);

        int count = 0;
        for (CarSalesEntity carSalesEntity : recommendsEntity) {
            if (count >= 3) break;
            int recommend_discount_price = carSalesEntity.getDiscountPrice();
            LocalDateTime recommend_create_date = carSalesEntity.getCreatedAt();
            boolean recommend_isLiked = carSalesLikeRepository.findByCarSalesIdUserId(carSalesEntity.getCarSalesId(), userId);

            SimilarCarResponseDto similarCarDto =SimilarCarResponseDto.builder()
                    .carId(carSalesEntity.getCar().getCarId())
                    .imageUrl(carSalesEntity.getCar().getImages().get(0).getImageUrl())
                    .brand(carSalesEntity.getCar().getCarModel().getBrand())
                    .model_name(carSalesEntity.getCar().getCarModel().getModelName())
                    .price(carSalesEntity.getPrice())
                    .discount_price(recommend_discount_price)
                    .isLiked(recommend_isLiked)
                    .build();
            similarCarResponseDtos.add(similarCarDto); // 리스트에 추가
            count++;
        }



        DetailCarResponseDto result = DetailCarResponseDto.builder()
                .created_at(carSales.getCreatedAt())
                .price(price)
                .discount_price(discount_price)
                .progress(progress)
                .agency_id(agency_id)
                .agency_name(agency_name)
                .latitude(latitude)
                .longitude(longitude)
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
                .recommendCars(similarCarResponseDtos)
                .isLiked(isLiked)
                .build();

        return result;
    }

    //차량 비교하기 서비스
    @Override
    public List<DetailCarResponseDto> compareCars(List<Long> carIds, long userId) {
        List<DetailCarResponseDto> result = new ArrayList<>();
        for(Long carId : carIds){
            CarSalesEntity carSales = carSalesRepository.findByCarId(carId);
            LocalDateTime create_date = carSales.getCreatedAt();
            int price = carSales.getPrice();
            int discount_price = carSales.getDiscountPrice();
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
            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(carSales.getCarSalesId(), userId);

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
                    .isLiked(isLiked)
                    .build();
            result.add(dto);
        }
        return result;
    }
    //상세보기 페이지 이동 시 조회수 증가 서비스carSalesRepository.deleteByCarId(userId, carId
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

    //구매 내역 조회 서비스
    @Override
    public List<TransactionStatusResponseDto> viewTransaction(long userId) {
        List<CarSalesEntity> carSalesEntities = carSalesRepository.findByUserId(userId);
        List<TransactionStatusResponseDto> results = new ArrayList<>();
        for(CarSalesEntity carSalesEntity : carSalesEntities){
            TransactionStatusResponseDto transactionStatusResponseDto = TransactionStatusResponseDto.builder()
                    .car_sales_id(carSalesEntity.getCarSalesId())
                    .sales_date(carSalesEntity.getUpdatedAt())
                    .progress(carSalesEntity.getProgress())
                    .brand(carSalesEntity.getCar().getCarModel().getBrand())
                    .model_name(carSalesEntity.getCar().getCarModel().getModelName())
                    .order_number(carSalesEntity.getOrderNumber())
                    .price(carSalesEntity.getPrice())
                    .imageUrl(carSalesEntity.getCar().getImages().get(0).getImageUrl())
                    .build();
            results.add(transactionStatusResponseDto);
        }
        return results;
    }

    //판매 내역 조회 서비스
    @Override
    public List<UserCarTransactionStatusResponseDto> viewUserCarTransaction(long userId) {
        List<CarPurchaseEntity> carPurchaseEntities = carPurchaseRepository.findByUserId(userId);
        List<UserCarTransactionStatusResponseDto> results = new ArrayList<>();
        for(CarPurchaseEntity carPurchaseEntity : carPurchaseEntities){
            UserCarTransactionStatusResponseDto userCarTransactionStatusResponseDto = UserCarTransactionStatusResponseDto.builder()
                    .car_purchase_id(carPurchaseEntity.getCarPurchaseId())
                    .purchase_date(carPurchaseEntity.getPurchaseDate())
                    .progress(carPurchaseEntity.getProgress())
                    .brand(carPurchaseEntity.getCar().getCarModel().getBrand())
                    .model_name(carPurchaseEntity.getCar().getCarModel().getModelName())
                    .price(carPurchaseEntity.getPrice())
                    .imageUrl(carPurchaseEntity.getCar().getImages().get(0).getImageUrl())
                    .build();
            results.add(userCarTransactionStatusResponseDto);
        }
        return results;
    }

    //찜한 상품 조회 서비스
    @Override
    public List<IsHeartCarResponseDto> viewIsHeartCar(long userId) {
        List<CarSalesLikeEntity> carSalesLikeEntities = carSalesLikeRepository.findByUserId(userId);
        List<IsHeartCarResponseDto> result = new ArrayList<>();
        for (CarSalesLikeEntity car_sales_like : carSalesLikeEntities) {
            IsHeartCarResponseDto isHeart_car;
            long carId = car_sales_like.getCarSales().getCar().getCarId();
            String imageUrl = car_sales_like.getCarSales().getCar().getImages().get(0).getImageUrl();
            String brand = car_sales_like.getCarSales().getCar().getCarModel().getBrand();
            String model_name = car_sales_like.getCarSales().getCar().getCarModel().getModelName();
            String model_year = car_sales_like.getCarSales().getCar().getCarModel().getModelYear();
            int distance = car_sales_like.getCarSales().getCar().getDistance();
            int price = car_sales_like.getCarSales().getPrice();
            int discount_price = car_sales_like.getCarSales().getDiscountPrice();
            int month_price = price / 6;

            LocalDateTime create_date = car_sales_like.getCarSales().getCreatedAt();
            int view_count = car_sales_like.getCarSales().getCount();
            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales_like.getCarSales().getCarSalesId(), userId);

            isHeart_car = IsHeartCarResponseDto.builder()
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
                    .isLiked(isLiked)
                    .build();

            result.add(isHeart_car);
        }
        return result.isEmpty() ? null : result;
    }

    //사용자 기반 추천 차량 조회(메인페이지) 서비스
    @Override
    public List<RecommendCarResponseDto> viewUserCarRecommend(long userId) {
        // 사용자에 대한 추천 정보를 조회
        RecommendEntity recommend = recommendRepository.findByUserId(userId);

        List<RecommendCarResponseDto> result = new ArrayList<>();

        // 1번부터 9번까지 반복하여 추천 차량을 조회
        for (int i = 1; i <= 9; i++) {

            Long recommendCarId = getRecommendCarIdByIndex(recommend, i); // 인덱스에 맞는 차량 ID 가져오기

            // 추천된 차량 ID가 -1이면 반복문 종료
            if (recommendCarId == -1) {
                return result;
            }
            // 추천된 차량 정보 조회
            CarSalesEntity carSalesEntity = carSalesRepository.findByCarSalesId(recommendCarId);

            if (carSalesEntity != null) {
                int price = carSalesEntity.getPrice();
                int discount_price = carSalesEntity.getDiscountPrice();
                int month_price = price / 6;
                LocalDateTime create_date = carSalesEntity.getCreatedAt();
                boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(carSalesEntity.getCarSalesId(), userId);

                // DTO 생성
                RecommendCarResponseDto recommendCarResponseDto = RecommendCarResponseDto.builder()
                        .carId(carSalesEntity.getCar().getCarId())
                        .imageUrl(carSalesEntity.getCar().getImages().get(0).getImageUrl())
                        .brand(carSalesEntity.getCar().getCarModel().getBrand())
                        .model_name(carSalesEntity.getCar().getCarModel().getModelName())
                        .model_year(carSalesEntity.getCar().getCarModel().getModelYear())
                        .distance(carSalesEntity.getCar().getDistance())
                        .price(carSalesEntity.getCar().getPrice())
                        .discount_price(discount_price)
                        .month_price(month_price)
                        .create_date(create_date)
                        .view_count(carSalesEntity.getCount())
                        .isLiked(isLiked)
                        .build();

                // 결과 리스트에 추가
                result.add(recommendCarResponseDto);
            }
        }
        return result;
    }

    //차량 계약 서비스
    @Override
    public void contractCar(long userId, long carId) {
        CarSalesEntity carSalesEntity = carSalesRepository.findByCarId(carId);
        UserEntity userEntity = userRepository.findByUserId(userId);
        long carSalesId = carSalesEntity.getCarSalesId();
        Boolean isVisible = carSalesEntity.isVisible();
        int price = carSalesEntity.getPrice();
        LocalDateTime sales_date = LocalDateTime.now();
        AgencyEntity agencyEntity = carSalesEntity.getAgency();
        CarEntity carEntity = carSalesEntity.getCar();
        int view_count = carSalesEntity.getCount();
        List<CarSalesLikeEntity> likes = carSalesEntity.getLikes();
        String orderNumber = UUID.randomUUID().toString().replace("-", "");

        carSalesEntity = CarSalesEntity.builder()
                .carSalesId(carSalesId)
                .isVisible(isVisible)
                .price(price)
                .salesDate(sales_date)
                .agency(agencyEntity)
                .user(userEntity)
                .progress("거래중")
                .car(carEntity)
                .count(view_count)
                .likes(likes)
                .orderNumber(orderNumber)
                .build();
        carSalesRepository.save(carSalesEntity);
    }

    @Override
    public List<SearchCarResponseDto> searchCarsBrandAndModelName(String brand, String modelName, long userId) {
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
            int discount_price = car_sales.getDiscountPrice();
            int month_price = price / 6;

            LocalDateTime create_date = car_sales.getCreatedAt();

            int view_count = car_sales.getCount();
            boolean isLiked = carSalesLikeRepository.findByCarSalesIdUserId(car_sales.getCarSalesId(), userId);

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
                    .isLiked(isLiked)
                    .build();

            result.add(search_car);
        }
        return result.isEmpty() ? null : result;
    }

    // 추천된 차량 ID를 인덱스에 맞게 가져오는 메서드
    private Long getRecommendCarIdByIndex(RecommendEntity recommend, int index) {
        switch (index) {
            case 1: return recommend.getRecommendCar1Id();
            case 2: return recommend.getRecommendCar2Id();
            case 3: return recommend.getRecommendCar3Id();
            case 4: return recommend.getRecommendCar4Id();
            case 5: return recommend.getRecommendCar5Id();
            case 6: return recommend.getRecommendCar6Id();
            case 7: return recommend.getRecommendCar7Id();
            case 8: return recommend.getRecommendCar8Id();
            case 9: return recommend.getRecommendCar9Id();
            default: return null;
        }
    }

    //차량 discount 업데이트 메소드
    @Override
    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    public void updateDiscountPrice() {
        log.info("시작1!");
        List<CarSalesEntity> carSalesEntities = carSalesRepository.findSalesCar();
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        for(CarSalesEntity carSalesEntity : carSalesEntities) {
            if(carSalesEntity.getCreatedAt().isBefore(oneWeekAgo)) {  // 생성일이 1주일 이상 지났는지 체크
                int originalPrice = carSalesEntity.getPrice();
                int currentPrice = carSalesEntity.getDiscountPrice() == 0 ? originalPrice : carSalesEntity.getDiscountPrice();
                int discountPrice = currentPrice - (int)(originalPrice * 0.03);   // 1주일이 지날때마다 원가의 3퍼센트씩 할인되도록 설정

                // 해당 차량을 관심 차량으로 등록한 유저들을 불러오기 위한 부분
                List<CarSalesLikeEntity> salesLikeLIst = carSalesLikeRepository.findByCarSales(carSalesEntity);
                log.info(salesLikeLIst);

                String title = "관심 차량의 가격이 인하되었습니다";
                String body = carSalesEntity.getCar().getCarModel().getModelName() + " " + carSalesEntity.getCar().getCarModel().getModelYear()
                        + " " + carSalesEntity.getCar().getCarNumber() + "\n"
                        + currentPrice + "원 -> " + discountPrice + "원";

                carSalesEntity.setDiscountPrice(discountPrice);
                carSalesRepository.save(carSalesEntity);  // 변경사항 저장

                for(CarSalesLikeEntity salesLike : salesLikeLIst) {
                    NotificationRequestDto notification = NotificationRequestDto.builder()
                            .user(salesLike.getUser())
                            .notificationType(1)
                            .title(title)
                            .content(body)
                            .build();

                    try{
                        log.info("알림 전송");
                        fcmService.sendMessageTo(salesLike.getUser().getFcmToken(), title, body);
                        log.info("알림 저장");
                        notificationService.addNotification(notification);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    @Override
    public void isLikedCar(UserEntity user, long carId) {
        CarSalesEntity carSalesEntity = carSalesRepository.findByCarId(carId);
        CarSalesLikeEntity carSalesLikeEntity = CarSalesLikeEntity.builder()
                .carSales(carSalesEntity)
                .user(user)
                .build();
        carSalesLikeRepository.save(carSalesLikeEntity);
    }

    @Override
    public void unLikedCar(UserEntity user, long carId) {
        CarSalesEntity carSalesEntity = carSalesRepository.findByCarId(carId);
        carSalesLikeRepository.deleteByUserIdCarId(user.getUserId(), carSalesEntity.getCarSalesId());
    }
}
