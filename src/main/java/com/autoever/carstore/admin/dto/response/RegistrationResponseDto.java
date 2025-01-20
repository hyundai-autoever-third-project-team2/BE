package com.autoever.carstore.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationResponseDto {
    private boolean isVisible;
    private long carSalesId;
    private long carId;
    private int distance;
    private boolean navigation;
    private boolean hud;
    private boolean ventilatedSeat;
    private boolean heatedSeat;
    private boolean cruiseControl;
    private boolean sunroof;
    private boolean parkingDistanceWarning;
    private boolean lineOutWarning;
    private String carImage;
    private String carBrand;
    private String carModel;

}
