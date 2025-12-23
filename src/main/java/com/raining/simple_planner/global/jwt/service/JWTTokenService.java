package com.raining.simple_planner.global.jwt.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raining.simple_planner.domain.user.document.User;
import com.raining.simple_planner.domain.user.dto.LoginRequestDTO;
import com.raining.simple_planner.domain.user.dto.TokenRefreshRequestDTO;
import com.raining.simple_planner.domain.user.exception.AuthenticationFailException;
import com.raining.simple_planner.domain.user.exception.UserNotFoundException;
import com.raining.simple_planner.domain.user.repository.UserRepository;
import com.raining.simple_planner.global.jwt.document.BlackList;
import com.raining.simple_planner.global.jwt.document.RefreshToken;
import com.raining.simple_planner.global.jwt.dto.TokenInfo;
import com.raining.simple_planner.global.jwt.exception.TokenRefreshFailException;
import com.raining.simple_planner.global.jwt.provider.JwtTokenProvider;
import com.raining.simple_planner.global.jwt.repository.BlackListRepository;
import com.raining.simple_planner.global.jwt.repository.RefreshTokenRepository;
import com.raining.simple_planner.global.util.TokenUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlackListRepository blackListRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider provider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenInfo login(LoginRequestDTO loginRequestDTO) {
        log.info("로그인 시도 | ID : {}", loginRequestDTO.getId());

        User user = userRepository.findById(loginRequestDTO.getId())
                .orElseThrow(UserNotFoundException::new);

        // 1. 비밀번호 검증 (필수)
        if (!passwordEncoder.matches(
                loginRequestDTO.getPassword(),
                user.getPassword())) {
            throw new AuthenticationFailException();
        }

        // 3. JWT 발급
        TokenInfo tokenInfo = generateNewTokens(user.getId(), user.getRole().name());

        // 4. RefreshToken 저장
        saveRefreshToken(RefreshToken.builder()
                .tokenKey(user.getId())
                .tokenValue(tokenInfo.getRefreshToken())
                .build());

        log.info("로그인 성공 | ID : {}", loginRequestDTO.getId());
        return tokenInfo;
    }

    /**
     * 로그아웃 처리
     * @param token
     */
    @Transactional
    public void logout(String token) {
        // 토큰을 블랙리스트에 추가
        blackListRepository.save(new BlackList(token));

        String userLoginId = TokenUtil.getUserLoginId(token);

        // 리프레시 토큰 삭제
        refreshTokenRepository.deleteById(userLoginId);

        log.info("User logged out. Token added to blacklist and refresh token deleted : {}", userLoginId);
    }

    /**
     * 토큰 재발급
     * @param requestDTO
     */
    @Transactional
    public TokenInfo refreshToken(TokenRefreshRequestDTO requestDTO) {
        String userLoginId = TokenUtil.getUserLoginId(requestDTO.getAccessToken());
        RefreshToken storedRefreshToken = refreshTokenRepository.findById(userLoginId)
                .orElseThrow(TokenRefreshFailException::new);
        
        if (!storedRefreshToken.getTokenValue().equals(requestDTO.getRefreshToken())) {
            throw new TokenRefreshFailException();
        }

        // 새로운 토큰 생성
        TokenInfo newToken = generateNewTokens(userLoginId, TokenUtil.getPrimaryRole(requestDTO.getAccessToken()));

        // 리프레시 토큰 저장소 업데이트
        storedRefreshToken.setTokenValue(newToken.getRefreshToken());
        saveRefreshToken(storedRefreshToken);

        log.info("토큰 재발급 성공 | ID : {}", userLoginId);
        return newToken;
    }

    private TokenInfo generateNewTokens(String userLoginId, String role) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                    userLoginId,
                    null,
                    List.of(new SimpleGrantedAuthority(role))
                );

        return provider.generateToken(authenticationToken);
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
        log.info("Refresh token saved for user ID : {}", refreshToken.getTokenKey());
    }

}
