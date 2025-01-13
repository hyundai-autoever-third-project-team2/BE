//package com.autoever.carstore.oauth2;
//
//import com.autoever.carstore.jwt.JWTUtil;
//import com.autoever.carstore.oauthjwt.dto.CustomOAuth2User;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//
//@Component
//public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final JWTUtil jwtUtil;
//
//    public CustomSuccessHandler(JWTUtil jwtUtil) {
//
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//        //OAuth2User
//        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
//
//        String username = customUserDetails.getUsername();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//        String role = auth.getAuthority();
//        String email = customUserDetails.getEmail();
//
//        String token = jwtUtil.createJwt(username, role, email, 60*60*60L);
//
//        response.addCookie(createCookie("Authorization", token));
//        response.sendRedirect("http://localhost:3000/");
//    }
//
//    private Cookie createCookie(String key, String value) {
//
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(60*60*60);
//        //cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//
//        return cookie;
//    }
//}

package com.autoever.carstore.oauth2;

import com.autoever.carstore.jwt.JWTUtil;
import com.autoever.carstore.oauthjwt.dto.CustomOAuth2User;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public CustomSuccessHandler(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        String email = customUserDetails.getEmail();

        // 권한(Role) 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        String role = iterator.hasNext() ? iterator.next().getAuthority() : "ROLE_USER";

        // JWT 토큰 생성
        String accessToken = jwtUtil.createAccessToken(username, role, email);
        String refreshToken = jwtUtil.createRefreshToken(username);

        // Refresh Token을 User Entity에 저장
        UserEntity user = userRepository.findByUsername(username); // DB에서 사용자 조회
        if (user != null) {
            user.updateRefreshToken(refreshToken); // Refresh Token 갱신
            userRepository.save(user); // 변경사항 저장
        }

        // 쿠키에 Access Token 저장
        response.addCookie(createCookie("AccessToken", accessToken,  60 * 60 * 24)); // 24시간 유효
        // 쿠키에 Refresh Token 저장
        response.addCookie(createCookie("RefreshToken", refreshToken, 60 * 60 * 24 * 7)); // 7일 유효

        // 역할(Role)에 따른 리디렉션
        if ("ROLE_ADMIN".equals(role)) {
            response.sendRedirect("http://localhost:3000/admin"); // Admin 사용자 리디렉션
        } else {
            response.sendRedirect("http://localhost:3000/"); // 일반 사용자 리디렉션
        }
    }

    private Cookie createCookie(String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // HTTPS 환경에서 활성화
        return cookie;
    }
}
