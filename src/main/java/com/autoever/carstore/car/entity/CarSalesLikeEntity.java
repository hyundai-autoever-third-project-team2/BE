package com.autoever.carstore.car.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CarSalesLikeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_sales_like_id")
    private long carSalesLikeId;

    @ManyToOne
    @JoinColumn(name = "car_sales_id", nullable = false)
    private CarSalesEntity carSales;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
