package com.autoever.carstore.reservation.service;

import com.autoever.carstore.agency.dao.AgencyRepository;
import com.autoever.carstore.agency.entity.AgencyEntity;
import com.autoever.carstore.oauthjwt.util.SecurityUtil;
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
    private final SecurityUtil securityUtil;

    @Override
    public ReservationResponseDto makeReservation(Long agencyId, LocalDateTime time) {
        UserEntity user = securityUtil.getLoginUser();
        if(user == null) {
            throw new IllegalArgumentException("로그인된 유저가 없습니다.");
        }
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
    public List<ReservationResponseDto> getReservationList() {
        UserEntity user = securityUtil.getLoginUser();
        if(user == null) {
            throw new IllegalArgumentException("로그인된 유저가 없습니다.");
        }

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
    public void deleteReservation(Integer reservationId) {
        UserEntity user = securityUtil.getLoginUser();
        if(user == null) {
            throw new IllegalArgumentException("로그인된 유저가 없습니다.");
        }

        // Optional 처리
        ReservationEntity reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 reservationId에 대한 예약이 없습니다."));

        System.out.println(reservation);

        if(!reservation.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("해당 유저의 예약이 아닙니다.");
        }

        reservationRepository.deleteById(reservationId);
    }
}
