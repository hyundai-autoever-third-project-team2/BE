package com.autoever.carstore.reservation.service;

import com.autoever.carstore.agency.dao.AgencyRepository;
import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.reservation.dao.ReservationRepository;
import com.autoever.carstore.reservation.dto.ReservationResponseDto;
import com.autoever.carstore.reservation.entity.ReservationEntity;
import com.autoever.carstore.user.dao.UserRepository;
import com.autoever.carstore.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;

    @Override
    public ReservationResponseDto makeReservation(Long userId, Long agencyId, LocalDateTime time) {

        UserEntity user = userRepository.findById(userId).orElseThrow();
        AgencyEntity agency = agencyRepository.findById(agencyId).orElseThrow();

        ReservationEntity reservation = reservationRepository.save(ReservationEntity.builder()
                                                            .user(user)
                                                            .agency(agency)
                                                            .reservationDate(time)
                                                            .build());

        ReservationResponseDto reservationResponseDto = ReservationResponseDto.builder()
                .reservationId(reservation.getReservationId())
                .agencyId(reservation.getAgency().getAgencyId())
                .agencyName(reservation.getAgency().getAgencyName())
                .userId(reservation.getUser().getUserId())
                .nickname(reservation.getUser().getNickname())
                .reservationDate(reservation.getReservationDate())
                .build();

        return reservationResponseDto;
    }

    @Override
    public List<ReservationResponseDto> getReservationList(Long userId) {

        UserEntity user = userRepository.findById(userId).orElseThrow();

        List<ReservationEntity> li = reservationRepository.findByUser(user);

        return li.stream()
                .map(reservation -> ReservationResponseDto.builder()
                        .reservationId(reservation.getReservationId())
                        .agencyId(reservation.getAgency().getAgencyId())
                        .agencyName(reservation.getAgency().getAgencyName())
                        .userId(reservation.getUser().getUserId())
                        .nickname(reservation.getUser().getNickname())
                        .reservationDate(reservation.getReservationDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReservation(Integer reservationId, Long userId) {

        // Optional 처리
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 reservationId에 대한 예약이 없습니다."));

        System.out.println(reservation);

        if(!reservation.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 유저의 예약이 아닙니다.");
        }

        reservationRepository.deleteById(reservationId);
    }
}
