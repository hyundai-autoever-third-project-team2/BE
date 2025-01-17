package com.autoever.carstore.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponseDto {
    String email;
    String nickname;
    String profileImage;
    String refreshToken;
    String userName;
}
