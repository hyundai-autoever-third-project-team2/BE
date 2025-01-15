package com.autoever.carstore.oauthjwt.util;

import com.autoever.carstore.oauthjwt.dto.CustomOAuth2User;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    private final UserRepository userRepository;

    public UserEntity getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();

        String email = user.getUsername();

        // 인증되지 않은 사용자인 경우 (anonymous) 예외 처리
        if (!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("인증되지 않은 사용자");  // 예외 발생
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("이메일이 존재하지 않습니다"));
    }

}
