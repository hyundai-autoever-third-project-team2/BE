package com.autoever.carstore.reservation.entity;

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
@Table(name="reservation")
public class ReservationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private int reservationId;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private AgencyEntity agency;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    public void updateAgency(AgencyEntity newAgency) {
        this.agency = newAgency;
    }

    public void updateReservationDate(LocalDateTime newReservationDate) {
        this.reservationDate = newReservationDate;
    }
}
