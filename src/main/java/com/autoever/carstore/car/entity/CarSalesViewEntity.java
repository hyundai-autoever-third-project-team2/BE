package com.autoever.carstore.car.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="car_sales_view")
public class CarSalesViewEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_sales_view_id")
    private long carSalesViewId;

    @ManyToOne
    @JoinColumn(name = "car_sales_id", nullable = false)
    private CarSalesEntity carSales;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
