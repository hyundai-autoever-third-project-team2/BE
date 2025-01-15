//package com.autoever.carstore.oauth2;
//
//import com.autoever.carstore.jwt.JWTUtil;
//import com.autoever.carstore.oauthjwt.dto.CustomOAuth2User;
//import com.autoever.carstore.user.entity.UserEntity;
//import com.autoever.carstore.user.dao.UserRepository;
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
//    private final UserRepository userRepository;
//
//    public CustomSuccessHandler(JWTUtil jwtUtil, UserRepository userRepository) {
//        this.jwtUtil = jwtUtil;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        // OAuth2User
//        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
//
//        String username = customUserDetails.getUsername();
//        String email = customUserDetails.getEmail();
//
//        // 권한(Role) 가져오기
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        String role = iterator.hasNext() ? iterator.next().getAuthority() : "ROLE_USER";
//
//        // JWT 토큰 생성
//        String accessToken = jwtUtil.createAccessToken(username, role, email);
//        String refreshToken = jwtUtil.createRefreshToken(username);
//
//        // Refresh Token을 User Entity에 저장
//        UserEntity user = userRepository.findByUsername(username); // DB에서 사용자 조회
//        if (user != null) {
//            user.updateRefreshToken(refreshToken); // Refresh Token 갱신
//            userRepository.save(user); // 변경사항 저장
//        }
//
//        // 쿠키에 Access Token 저장
//        response.addCookie(createCookie("AccessToken", accessToken,  60 * 60 * 24)); // 24시간 유효
//        // 쿠키에 Refresh Token 저장
//        response.addCookie(createCookie("RefreshToken", refreshToken, 60 * 60 * 24 * 7)); // 7일 유효
//
//        // 역할(Role)에 따른 리디렉션
//        if ("ROLE_ADMIN".equals(role)) {
//            // Admin 사용자 리디렉션
////            response.sendRedirect("https://twomuchcar.shop/admin");
//            response.sendRedirect("http://localhost:8080/admin/home");
//        } else {
//            response.sendRedirect("https://twomuchcar.shop/"); // 일반 사용자 리디렉션
//        }
//    }
//
//    private Cookie createCookie(String key, String value, int maxAge) {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(maxAge);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setDomain("frontend.com");
//        // HTTPS 환경에서 활성화
//        //cookie.setSecure(true);
//
//        return cookie;
//    }
//}
//

package com.autoever.carstore.oauth2;

import com.autoever.carstore.jwt.JWTUtil;
import com.autoever.carstore.oauthjwt.dto.CustomOAuth2User;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.dao.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
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
        response.addHeader("Set-Cookie", createSecureCookie("AccessToken", accessToken, 60 * 60 * 24)); // 24시간 유효
        // 쿠키에 Refresh Token 저장
        response.addHeader("Set-Cookie", createSecureCookie("RefreshToken", refreshToken, 60 * 60 * 24 * 7)); // 7일 유효

        // 역할(Role)에 따른 리디렉션
        if ("ROLE_ADMIN".equals(role)) {
            response.sendRedirect("http://localhost:8080/admin/home"); // Admin 사용자 리디렉션
        } else {
            response.sendRedirect("https://autoever.site"); // 일반 사용자 리디렉션
        }
    }

    private String createSecureCookie(String name, String value, int maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                // HTTPS 환경에서만 동작
                .secure(true)
                .sameSite("None") // SameSite 설정
                .path("/")
                .maxAge(maxAge)
                .domain("autoever.site") // 쿠키의 도메인 설정
                .build()
                .toString();
    }
}

