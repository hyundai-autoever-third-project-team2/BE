package com.autoever.carstore.reservation.service;

import com.autoever.carstore.reservation.dto.ReservationResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    ReservationResponseDto makeReservation(Long userId, Long agencyId, LocalDateTime time);

    List<ReservationResponseDto> getReservationList(Long userId);

    void deleteReservation(Integer reservationId, Long id);
}
