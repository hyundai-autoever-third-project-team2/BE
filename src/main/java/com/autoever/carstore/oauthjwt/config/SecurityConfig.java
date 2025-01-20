package com.autoever.carstore.oauthjwt.config;

import com.autoever.carstore.jwt.JWTFilter;
import com.autoever.carstore.jwt.JWTUtil;
import com.autoever.carstore.oauth2.CustomSuccessHandler;
import com.autoever.carstore.oauthjwt.service.CustomOAuth2UserService;
import com.autoever.carstore.user.dao.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserRepository userRepository) throws Exception {

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //JWTFilter 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class);

        //JWTFilter 위치 조정
        http
                .addFilterAfter(new JWTFilter(jwtUtil, userRepository), OAuth2LoginAuthenticationFilter.class);

        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler));

        //경로별 인가 작업
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/**", "/admin/chat","/css/**", "/js/**", "/admin/chat/**").permitAll() // 누구나 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN만 접근 가능
                        .requestMatchers("/user/**").authenticated() // 로그인한 사용자만 접근 가능
                        .anyRequest().permitAll() // 그 외의 요청은 인증 없이 접근 가능
        );

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //cors 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(
                                Arrays.asList(
                                        "https://twomuchcar.shop",  // 운영 도메인
                                        "https://autoever.site",
                                        "http://localhost:8080",    // 로컬 테스트 도메인
                                        "http://localhost:5173"
                                )
                        );
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.addExposedHeader("Set-Cookie");
                        configuration.addExposedHeader("Authorization");
                        configuration.setMaxAge(3600L);


                        return configuration;
                    }
                }));

        return http.build();
    }
}