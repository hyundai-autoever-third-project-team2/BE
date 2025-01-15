package com.autoever.carstore.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;
    private long accessTokenExpiredIn;
    private long refreshTokenExpiredIn;

    public JWTUtil(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.access-expired-in}") long accessTokenExpiredIn,
            @Value("${spring.jwt.refresh-expired-in}") long refreshTokenExpiredIn
    ) {
        this.accessTokenExpiredIn = accessTokenExpiredIn;
        this.refreshTokenExpiredIn = refreshTokenExpiredIn;
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // Access Token 생성
    public String createAccessToken(String username, String role, String email) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .claim("email", email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiredIn))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(String username) {
        return Jwts.builder()
                .claim("username", username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiredIn))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Access Token에서 사용자 이름 추출
    public String getUsernameFromAccessToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    // Access Token에서 역할(Role) 추출
    public String getRoleFromAccessToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // Access Token에서 이메일 추출
    public String getEmailFromAccessToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    // Access Token 만료 여부 확인
    public Boolean isAccessTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Refresh Token 만료 여부 확인
    public Boolean isRefreshTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
