package com.autoever.carstore.recommend.entity;

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
public class RecommendEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommend_id")
    private int recommendId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "recommend_car1_id", nullable = false)
    private int recommendCar1Id;

    @Column(name = "recommend_car2_id", nullable = false)
    private int recommendCar2Id;

    @Column(name = "recommend_car3_id", nullable = false)
    private int recommendCar3Id;

    @Column(name = "recommend_car4_id", nullable = false)
    private int recommendCar4Id;

    @Column(name = "recommend_car5_id", nullable = false)
    private int recommendCar5Id;

    @Column(name = "recommend_car6_id", nullable = false)
    private int recommendCar6Id;

    @Column(name = "recommend_car7_id", nullable = false)
    private int recommendCar7Id;

    @Column(name = "recommend_car8_id", nullable = false)
    private int recommendCar8Id;

    @Column(name = "recommend_car9_id", nullable = false)
    private int recommendCar9Id;

    public void updateRecommendCar1Id(int recommendCar1Id) {
        this.recommendCar1Id = recommendCar1Id;
    }

    public void updateRecommendCar2Id(int recommendCar2Id) {
        this.recommendCar2Id = recommendCar2Id;
    }

    public void updateRecommendCar3Id(int recommendCar3Id) {
        this.recommendCar3Id = recommendCar3Id;
    }

    public void updateRecommendCar4Id(int recommendCar4Id) {
        this.recommendCar4Id = recommendCar4Id;
    }

    public void updateRecommendCar5Id(int recommendCar5Id) {
        this.recommendCar5Id = recommendCar5Id;
    }

    public void updateRecommendCar6Id(int recommendCar6Id) {
        this.recommendCar6Id = recommendCar6Id;
    }

    public void updateRecommendCar7Id(int recommendCar7Id) {
        this.recommendCar7Id = recommendCar7Id;
    }

    public void updateRecommendCar8Id(int recommendCar8Id) {
        this.recommendCar8Id = recommendCar8Id;
    }

    public void updateRecommendCar9Id(int recommendCar9Id) {
        this.recommendCar9Id = recommendCar9Id;
    }
}
