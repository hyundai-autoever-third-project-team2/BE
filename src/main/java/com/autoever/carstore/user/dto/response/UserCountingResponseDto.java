package com.autoever.carstore.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCountingResponseDto {
    int purchaseCount;
    int saleCount;
    int heartCount;
}
