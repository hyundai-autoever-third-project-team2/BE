package com.autoever.carstore.reservation.service;

import com.autoever.carstore.reservation.dto.ReservationResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    ReservationResponseDto makeReservation(Long agencyId, LocalDateTime time);

    List<ReservationResponseDto> getReservationList();

    void deleteReservation(Integer reservationId);
}
