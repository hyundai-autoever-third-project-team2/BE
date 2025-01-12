package com.autoever.carstore.car.entity;

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
@Table(name="car_model")
public class CarModelEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_model_id")
    private long carModelId;

    @Column(nullable = false)
    private String brand;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "model_year", nullable = false)
    private String modelYear;

    @Column(nullable = false)
    private String fuel;

    @Column(nullable = false)
    private String gear;

    @Column(name = "fuel_efficiency", nullable = false)
    private double fuelEfficiency;

    @Column(name = "car_type", nullable = false)
    private String carType;

    @Column(nullable = false)
    private int displacement;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public void updateBrand(String brand) {
        this.brand = brand;
    }

    public void updateModelName(String modelName) {
        this.modelName = modelName;
    }

    public void updateModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public void updateFuel(String fuel) {
        this.fuel = fuel;
    }

    public void updateGear(String gear) {
        this.gear = gear;
    }

    public void updateFuelEfficiency(double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public void updateCarType(String carType) {
        this.carType = carType;
    }

    public void updateDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public void updateIsActive() {
        this.isActive = !this.isActive;
    }
}

