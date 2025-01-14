package com.autoever.carstore.agency.service;

import com.autoever.carstore.agency.dao.AgencyRepository;
import com.autoever.carstore.agency.dto.AgencyResponseDto;
import com.autoever.carstore.agency.entity.AgencyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgencyServiceImpl implements AgencyService {
    private final AgencyRepository agencyRepository;

    public List<AgencyResponseDto> getAgencyList(){
        List<AgencyEntity> agencies = agencyRepository.findByIsActiveTrue();

        return agencies.stream()
                .map(agency -> AgencyResponseDto.builder()
                        .agencyId(agency.getAgencyId())
                        .latitude(agency.getLatitude())
                        .longitude(agency.getLongitude())
                        .agencyName(agency.getAgencyName())
                        .build())
                .collect(Collectors.toList());
    }
}
