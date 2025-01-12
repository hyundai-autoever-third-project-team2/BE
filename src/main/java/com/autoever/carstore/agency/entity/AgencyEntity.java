package com.autoever.carstore.agency.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="agency")
public class AgencyEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agency_id")
    private int agencyId;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "agency_name", nullable = false)
    private String agencyName;

    public void updateLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void updateLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void updateIsActive() {
        this.isActive = !this.isActive;
    }

    public void updateAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
}
