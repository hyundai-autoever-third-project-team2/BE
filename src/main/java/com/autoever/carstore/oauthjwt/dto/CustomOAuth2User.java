package com.autoever.carstore.oauthjwt.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {
        // 사용자 정보를 Map 형태로 반환
        return Map.of(
                "username", userDTO.getUsername(),
                "email", userDTO.getEmail(),
                "name", userDTO.getName(),
                "profileImage", userDTO.getProfileImage()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자 권한 반환 (람다식 활용)
        return List.of(() -> userDTO.getRole());
    }

    @Override
    public String getName() {
        // OAuth2User에서 요구하는 이름 반환
        return userDTO.getName();
    }

    public String getUsername() {
        // 사용자 고유 식별자 반환
        return userDTO.getUsername();
    }

    public String getEmail() {
        // 사용자 이메일 반환
        return userDTO.getEmail();
    }

    public String getProfileImage() {
        // 사용자 프로필 이미지 URL 반환
        return userDTO.getProfileImage();
    }
}
