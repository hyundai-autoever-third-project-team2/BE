package com.autoever.carstore.survey.entity;

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
@Table(name="survey")
public class SurveyEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private long surveyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "min_model_year", nullable = false)
    private int minModelYear;

    @Column(name = "max_model_year", nullable = false)
    private int maxModelYear;

    @Column(name = "min_distance", nullable = false)
    private int minDistance;

    @Column(name = "max_distance", nullable = false)
    private int maxDistance;

    @Column(name = "min_price", nullable = false)
    private int minPrice;

    @Column(name = "max_price", nullable = false)
    private int maxPrice;

    public void updateMinModelYear(int minModelYear) {
        this.minModelYear = minModelYear;
    }

    public void updateMaxModelYear(int maxModelYear) {
        this.maxModelYear = maxModelYear;
    }

    public void updateMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public void updateMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void updateMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public void updateMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }
}
