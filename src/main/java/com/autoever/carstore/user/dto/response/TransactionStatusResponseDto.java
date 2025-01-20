package com.autoever.carstore.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionStatusResponseDto {
    long car_sales_id;
    long car_id;
    String imageUrl;
    LocalDateTime sales_date;
    String progress;
    String brand;
    String model_name;
    String order_number;
    int price;
}
