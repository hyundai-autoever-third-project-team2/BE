package com.autoever.carstore.oauthjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String username;       // 고유 사용자 식별자 (provider_id)
    private String email;          // 사용자 이메일
    private String name;           // 사용자 이름 (닉네임)
    private String profileImage;   // 사용자 프로필 이미지 URL
    private String role;           // 사용자 권한 (예: ROLE_USER)
}