package com.autoever.carstore.kakaopay.controller;

import com.autoever.carstore.kakaopay.dto.request.KakaoPayReadyRequestDto;
import com.autoever.carstore.kakaopay.dto.response.KakaoPayAproveResponseDto;
import com.autoever.carstore.kakaopay.dto.response.KakaoPayReadyResponseDto;
import com.autoever.carstore.kakaopay.service.KakaoPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/kakaopay")
public class KakaoPayController {

    @Autowired
    private KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public KakaoPayReadyResponseDto ready(@RequestBody KakaoPayReadyRequestDto request) {
        KakaoPayReadyResponseDto response = kakaoPayService.preparePayment(request);

        // 응답에 tid를 포함시켜 반환
        log.info("결제 고유번호: " + response.getTid());
        return response;
    }

    @GetMapping("/success")
    public ResponseEntity<? super KakaoPayAproveResponseDto> payCompleted(@RequestParam("pg_token") String pgToken, @RequestParam("tid") String tid) {  // URL 파라미터로 tid를 받음
        // 카카오 결제 요청하기
        KakaoPayAproveResponseDto approveResponse = kakaoPayService.payApprove(tid, pgToken);
        return ResponseEntity.ok(approveResponse);
    }
}