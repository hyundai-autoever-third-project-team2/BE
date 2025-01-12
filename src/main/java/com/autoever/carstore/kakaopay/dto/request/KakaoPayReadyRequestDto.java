package com.autoever.carstore.kakaopay.dto.request;

import lombok.Data;

@Data
public class KakaoPayReadyRequestDto {
    private String cid = "TC0ONETIME"; // 테스트용 코드
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private int quantity;
    private int total_amount;
    private int tax_free_amount;
    private String approval_url; // 성공 시 리다이렉트 URL
    private String cancel_url;   // 취소 시 리다이렉트 URL
    private String fail_url;     // 실패 시 리다이렉트 URL
}
