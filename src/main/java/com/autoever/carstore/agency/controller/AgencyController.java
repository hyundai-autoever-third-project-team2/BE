package com.autoever.carstore.agency.controller;

import com.autoever.carstore.agency.dto.AgencyResponseDto;
import com.autoever.carstore.agency.service.AgencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyService agencyService;

    @GetMapping("/list")
    public ResponseEntity<?> getAgencyList() {

        List<AgencyResponseDto> result = agencyService.getAgencyList();

        return ResponseEntity.ok(result);
    }

}
