package com.autoever.carstore.oauthjwt.dto;


import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes; // 전체 응답 데이터
    private final Map<String, Object> kakaoAccount; // 'kakao_account' 데이터
    private final Map<String, Object> profile; // 'profile' 데이터

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        // 'kakao_account'와 'profile' 데이터 추출
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) kakaoAccount.get("profile");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        // 카카오의 고유 사용자 ID
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        // 'kakao_account'에 이메일 정보가 포함되어 있음
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        // 'profile'에서 닉네임 정보 추출
        return profile.get("nickname").toString();
    }

    public String getProfileImage() {
        // 'profile'에서 프로필 이미지 URL 추출
        return profile.get("profile_image_url").toString();
    }
}