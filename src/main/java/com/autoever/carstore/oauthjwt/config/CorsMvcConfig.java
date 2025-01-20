package com.autoever.carstore.oauthjwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
                .allowedOriginPatterns("http://localhost", "https://autoever.site", "https://twomuchcar.shop") // 와일드카드 허용 도메인

                .allowedOrigins("https://twomuchcar.shop",
                        "https://autoever.site",
                        "https://www.googleapis.com/auth/cloud-platform",
                        "https://fcm.googleapis.com",
                        "http://10.0.2.2:8080") // https://twomuchcar.shop만 허용
                .allowedMethods("*") // 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .exposedHeaders("Set-Cookie", "Authorization") // 클라이언트가 접근 가능한 헤더
                .allowCredentials(true); // 쿠키 포함 요청 허용
    }
}
