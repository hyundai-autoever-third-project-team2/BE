package com.autoever.carstore.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequestDto {
    private long carPurchaseId;
    private int price;
    private long agencyId;
    private boolean isVisible = true;
}
