package com.autoever.carstore.kakaopay.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayAproveResponseDto {
    private String aid;                 // 요청 고유 번호
    private String tid;                 // 결제 고유 번호
    private String cid;                 // 가맹점 코드
    private String partner_order_id;    // 가맹점 주문번호
    private String partner_user_id;     // 가맹점 회원 ID
    private String payment_method_type; // 결제 수단, CARD 또는 MONEY 중 하나
    private String item_name;           // 상품 이름
    private String item_code;           // 상품 코드 (JSON에 없으므로 optional)
    private int quantity;               // 상품 수량
    private Amount amount;              // 금액 정보
    private String created_at;          // 결제 준비 요청 시각
    private String approved_at;         // 결제 승인 시각
    private String payload;             // 결제 승인 요청 시 저장한 값

    @Getter
    @Setter
    @ToString
    public static class Amount {
        private int total;             // 총 결제 금액
        private int tax_free;          // 비과세 금액
        private int vat;               // 부가세 금액
        private int point;             // 사용한 포인트 금액
        private int discount;          // 할인 금액
        private int green_deposit;     // 녹색 매장 예치금
    }
}
