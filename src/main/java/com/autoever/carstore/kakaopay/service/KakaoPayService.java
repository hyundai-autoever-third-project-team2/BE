package com.autoever.carstore.kakaopay.service;

import com.autoever.carstore.kakaopay.dto.request.KakaoPayReadyRequestDto;
import com.autoever.carstore.kakaopay.dto.response.KakaoPayAproveResponseDto;
import com.autoever.carstore.kakaopay.dto.response.KakaoPayReadyResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoPayService {

    @Value("${kakaopay.admin-key}")
    private String adminKey;

    public KakaoPayReadyResponseDto preparePayment(KakaoPayReadyRequestDto request) {
        KakaoPayReadyResponseDto kakaoReady;
        // 카카오페이 요청 양식
        Map<String, String> params = new HashMap<>();
        params.put("cid", request.getCid());  // 테스트용 CID
        params.put("partner_order_id", request.getPartner_order_id());
        params.put("partner_user_id", request.getPartner_user_id());
        params.put("item_name", request.getItem_name());
        params.put("quantity", String.valueOf(request.getQuantity()));
        params.put("total_amount", String.valueOf(request.getTotal_amount()));
        params.put("vat_amount", "10");
        params.put("tax_free_amount", "0");
        params.put("approval_url", "https://twomuchcar.shop/kakaopay/success");
        params.put("cancel_url", "https://twomuchcar.shop/kakaopay/cancel");
        params.put("fail_url", "https://twomuchcar.shop/kakaopay/fail");

        // 파라미터, 헤더
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoPayReadyResponseDto.class);

        return kakaoReady;
    }

    // 카카오페이 결제 승인
    // 사용자가 결제 수단을 선택하고 비밀번호를 입력해 결제 인증을 완료한 뒤,
    // 최종적으로 결제 완료 처리를 하는 단계
    public KakaoPayAproveResponseDto payApprove(String tid, String pgToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");              // 가맹점 코드(테스트용)
        parameters.put("tid", tid);                       // 결제 고유번호
        parameters.put("partner_order_id", "12345"); // 주문번호
        parameters.put("partner_user_id", "user123");    // 회원 아이디
        parameters.put("pg_token", pgToken);              // 결제승인 요청을 인증하는 토큰

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        KakaoPayAproveResponseDto approveResponse = template.postForObject(url, requestEntity, KakaoPayAproveResponseDto.class);

        return approveResponse;
    }

    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "SECRET_KEY " + adminKey;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/json");

        return httpHeaders;
    }
}
