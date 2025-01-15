package com.autoever.carstore.reservation.controller;

import com.autoever.carstore.reservation.dto.ReservationResponseDto;
import com.autoever.carstore.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/register")
    public ResponseEntity<?> makeReservation(@RequestParam Long userId,
                                             @RequestParam Long agencyId,
                                             @RequestParam LocalDateTime time){

        ReservationResponseDto result = reservationService.makeReservation(userId, agencyId, time);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/list")
    public ResponseEntity<?> makeReservation(@RequestParam Long userId){

        List<ReservationResponseDto> result = reservationService.getReservationList(userId);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReservation(@RequestParam Long userId,
            @RequestParam Integer reservationId){

        reservationService.deleteReservation(reservationId, userId);

        return ResponseEntity.ok("success");
    }
}
