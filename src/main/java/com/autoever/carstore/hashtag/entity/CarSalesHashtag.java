package com.autoever.carstore.hashtag.entity;

import com.autoever.carstore.car.entity.CarSalesEntity;
import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
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
public class CarSalesHashtag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_sales_hashtag_id")
    private long carSalesHashtagId;

    @ManyToOne
    @JoinColumn(name = "car_sales_id", nullable = false)
    private CarSalesEntity carSales;

    @ManyToOne
    @JoinColumn(name = "hashtag_id", nullable = false)
    private HashtagEntity hashtag;
}
