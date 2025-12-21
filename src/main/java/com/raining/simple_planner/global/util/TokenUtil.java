package com.raining.simple_planner.global.util;

import java.util.Arrays;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public final class TokenUtil {

    @Value("${jwt.secret.key}")
    private static final String jwtSecret = "";

    private static SecretKey key;

    private TokenUtil() {
        // util class
    }

    /**
     * 애플리케이션 시작 시 1회 호출
     */
    public static void init(String base64SecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64SecretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT에서 Claims 추출 (만료 토큰 허용)
     */
    private static Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 사용자 ID (sub) 추출
     */
    public static String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Role 목록 추출
     * 예: ["ROLE_USER", "ROLE_ADMIN"]
     */
    public static List<String> getRoles(String token) {
        Object auth = parseClaims(token).get("auth");

        if (auth == null) {
            return List.of();
        }

        return Arrays.asList(auth.toString().split(","));
    }

    /**
     * 단일 Role만 필요한 경우
     */
    public static String getPrimaryRole(String token) {
        List<String> roles = getRoles(token);
        return roles.isEmpty() ? null : roles.get(0);
    }
}
