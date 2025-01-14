package com.autoever.carstore.agency.dao;

import com.autoever.carstore.agency.entity.AgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgencyRepository extends JpaRepository<AgencyEntity, Long> {
    List<AgencyEntity> findByIsActiveTrue();
}
