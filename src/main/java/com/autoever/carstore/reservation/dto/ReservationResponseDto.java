package com.autoever.carstore.reservation.dto;

import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDto {
    private int reservationId;
    private Integer agencyId;
    private String agencyName;
    private Long userId;
    private String nickname;
    private LocalDateTime reservationDate;
}
