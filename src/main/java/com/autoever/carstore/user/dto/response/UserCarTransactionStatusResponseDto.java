package com.autoever.carstore.user.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCarTransactionStatusResponseDto{
    long car_purchase_id;
    LocalDateTime purchase_date;
    String progress;
    String brand;
    String model_name;
    int price;
}
