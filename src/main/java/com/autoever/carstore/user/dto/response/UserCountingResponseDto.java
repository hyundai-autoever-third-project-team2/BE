package com.autoever.carstore.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCountingResponseDto {
    int purchaseCount = 0;
    int saleCount = 0;
    int heartCount = 0;
}
