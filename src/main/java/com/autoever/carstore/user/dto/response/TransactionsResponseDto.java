package com.autoever.carstore.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionsResponseDto {
    long car_sales_id;
    LocalDateTime sales_date;
    String progress;
    String brand;
    String model_name;
    String order_number;
    int price;
    Long userId;
    String userName;
}

