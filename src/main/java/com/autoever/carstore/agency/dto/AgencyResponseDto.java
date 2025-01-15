package com.autoever.carstore.agency.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgencyResponseDto {
    private int agencyId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String agencyName;
}
