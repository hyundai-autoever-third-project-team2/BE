package com.autoever.carstore.reservation.dao;

import com.autoever.carstore.reservation.entity.ReservationEntity;
import com.autoever.carstore.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    List<ReservationEntity> findByUser(UserEntity user);
}
