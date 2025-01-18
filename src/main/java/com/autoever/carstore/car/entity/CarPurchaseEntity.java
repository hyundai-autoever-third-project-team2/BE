package com.autoever.carstore.car.entity;

import com.autoever.carstore.common.entitiyBase.BaseTimeEntity;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="car_purchase")
public class CarPurchaseEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_purchase_id")
    private long carPurchaseId;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String comments;

    @Column(nullable = false)
    private String progress;

    @Column(nullable = false)
    private int price;

    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "carPurchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarPurchaseImageEntity> images;

    public void updateComments(String comments) {
        this.comments = comments;
    }

    public void updateProgress(String progress) {
        this.progress = progress;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updatePurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
