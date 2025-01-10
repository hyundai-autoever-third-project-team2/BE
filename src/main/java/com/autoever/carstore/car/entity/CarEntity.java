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
public class CarEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private long carId;

    @ManyToOne
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModelEntity carModel;

    @Column(name = "car_number", nullable = false)
    private String carNumber;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int distance;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private boolean navigation;

    @Column(nullable = false)
    private boolean hud;

    @Column(name = "ventilated_seat", nullable = false)
    private boolean ventilatedSeat;

    @Column(name = "heated_seat", nullable = false)
    private boolean heatedSeat;

    @Column(name = "cruise_control", nullable = false)
    private boolean cruiseControl;

    @Column(nullable = false)
    private boolean sunroof;

    @Column(name = "parking_distance_warning", nullable = false)
    private boolean parkingDistanceWarning;

    @Column(name = "line_out_warning", nullable = false)
    private boolean lineOutWarning;

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateDistance(int distance) {
        this.distance = distance;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void updateNavigation() {
        this.navigation = !this.navigation;
    }

    public void updateHud() {
        this.hud = !this.hud;
    }

    public void updateVentilatedSeat() {
        this.ventilatedSeat = !this.ventilatedSeat;
    }

    public void updateHeatedSeat() {
        this.heatedSeat = !this.heatedSeat;
    }

    public void updateCruiseControl() {
        this.cruiseControl = !this.cruiseControl;
    }

    public void updateSunroof() {
        this.sunroof = !this.sunroof;
    }

    public void updateParkingDistanceWarning() {
        this.parkingDistanceWarning = !this.parkingDistanceWarning;
    }

    public void updateLineOutWarning() {
        this.lineOutWarning = !this.lineOutWarning;
    }
}
