package com.autoever.carstore.jwt;

import com.autoever.carstore.oauthjwt.dto.CustomOAuth2User;
import com.autoever.carstore.oauthjwt.dto.UserDTO;
import com.autoever.carstore.user.entity.UserEntity;
import com.autoever.carstore.user.dao.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 쿠키에서 Access Token과 Refresh Token을 불러옴
        String accessToken = null;
        String refreshToken = null;


        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AccessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if ("RefreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // Access Token이 없는 경우
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Access Token 검증
        if (jwtUtil.isAccessTokenExpired(accessToken)) {
            // Refresh Token 검증
            if (refreshToken == null || jwtUtil.isRefreshTokenExpired(refreshToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Refresh Token이 유효하다면 DB에서 검증
            String username = jwtUtil.getUsernameFromAccessToken(refreshToken);
            UserEntity user = userRepository.findByUsername(username);

            if (user == null || !refreshToken.equals(user.getRefreshToken())) {
                filterChain.doFilter(request, response);
                return;
            }

            // Refresh Token 검증 성공 시 Access Token 재발급
            String role = jwtUtil.getRoleFromAccessToken(refreshToken);
            accessToken = jwtUtil.createAccessToken(username, role, null); // 이메일은 선택적으로 포함

            // 재발급된 Access Token을 쿠키에 추가
            Cookie newAccessTokenCookie = new Cookie("AccessToken", accessToken);
            newAccessTokenCookie.setHttpOnly(true);
            newAccessTokenCookie.setPath("/");
            response.addCookie(newAccessTokenCookie);
        }

        // Access Token에서 사용자 정보 추출
        String username = jwtUtil.getUsernameFromAccessToken(accessToken);
        String role = jwtUtil.getRoleFromAccessToken(accessToken);

        // UserDTO 생성 및 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);

        // CustomOAuth2User 생성 및 Spring Security 인증 토큰 설정
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}