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
    public ResponseEntity<?> makeReservation(@RequestParam Long agencyId,
                                             @RequestParam LocalDateTime time){

        ReservationResponseDto result = reservationService.makeReservation(agencyId, time);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/list")
    public ResponseEntity<?> makeReservation(){

        List<ReservationResponseDto> result = reservationService.getReservationList();

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReservation(@RequestParam Integer reservationId){

        reservationService.deleteReservation(reservationId);

        return ResponseEntity.ok("success");
    }
}
