package com.autoever.carstore.car.entity;

import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CarSalesEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_sales_id")
    private long carSalesId;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private AgencyEntity agency;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String progress;

    @Column(name = "sales_date")
    private LocalDateTime salesDate;

    @Column(name = "is_visible", nullable = false)
    private boolean isVisible;

    public void updateUser(UserEntity user) {
        this.user = user;
    }

    public void updateAgency(AgencyEntity agency) {
        this.agency = agency;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateProgress(String progress) {
        this.progress = progress;
    }

    public void updateSalesDate(LocalDateTime salesDate) {
        this.salesDate = salesDate;
    }

    public void updateIsVisible(boolean isVisible) {
        this.isVisible = !this.isVisible;
    }
}
