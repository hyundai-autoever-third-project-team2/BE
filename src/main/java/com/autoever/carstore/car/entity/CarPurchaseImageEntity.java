package com.autoever.carstore.car.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
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
@Table(name="car_purchase_image")
public class CarPurchaseImageEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_purchase_image_id")
    private long carPurchaseImageId;

    @ManyToOne
    @JoinColumn(name = "car_purchase_id", nullable = false)
    private CarPurchaseEntity carPurchase;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
