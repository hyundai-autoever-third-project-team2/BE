package com.autoever.carstore.kakaopay.controller;

import com.autoever.carstore.kakaopay.dto.request.KakaoPayReadyRequestDto;
import com.autoever.carstore.kakaopay.dto.response.KakaoPayAproveResponseDto;
import com.autoever.carstore.kakaopay.dto.response.KakaoPayReadyResponseDto;
import com.autoever.carstore.kakaopay.service.KakaoPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/kakaopay")
public class KakaoPayController {

    @Autowired
    private KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public KakaoPayReadyResponseDto ready(@RequestBody KakaoPayReadyRequestDto request) {
        // 카카오페이 준비 요청
        KakaoPayReadyResponseDto response = new KakaoPayReadyResponseDto();
        response = kakaoPayService.preparePayment(request);
        SessionUtils.addAttribute("tid", response.getTid());

        log.info("결제 고유번호: " + response.getTid());
        return response;
    }

    @GetMapping("/success")
    public String payCompleted(@RequestParam("pg_token") String pgToken) {

        String tid = SessionUtils.getStringAttributeValue("tid");

        // 카카오 결제 요청하기
        KakaoPayAproveResponseDto approveResponse = kakaoPayService.payApprove(tid, pgToken);

        return "success";
    }

}