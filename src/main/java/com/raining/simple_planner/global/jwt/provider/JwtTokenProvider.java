package com.raining.simple_planner.global.jwt.provider;

import com.raining.simple_planner.global.jwt.dto.TokenInfo;
import com.raining.simple_planner.global.jwt.exception.NoAuthInfoException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey key;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 5;        // 5분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24; // 1일

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token + Refresh Token 생성
     */
    public TokenInfo generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
            .subject(authentication.getName())
            .claim("auth", authorities)
            .expiration(accessTokenExpiresIn)
            .signWith(key)
            .compact();

        String refreshToken = Jwts.builder()
            .expiration(refreshTokenExpiresIn)
            .signWith(key)
            .compact();

        return TokenInfo.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .role(authorities)
            .build();
    }

    /**
     * JWT → Authentication 변환
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new NoAuthInfoException();
        }

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal =
            new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * JWT 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

            return true;

        } catch (SignatureException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 클레임이 비어있습니다.", e);
        }

        return false;
    }

    /**
     * Claims 파싱 (만료 토큰 허용)
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
