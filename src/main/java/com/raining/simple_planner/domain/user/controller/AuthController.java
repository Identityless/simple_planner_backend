package com.raining.simple_planner.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raining.simple_planner.domain.user.dto.LoginRequestDTO;
import com.raining.simple_planner.domain.user.dto.TokenRefreshRequestDTO;
import com.raining.simple_planner.domain.user.dto.UserRegisterDTO;
import com.raining.simple_planner.domain.user.exception.UserIdDupException;
import com.raining.simple_planner.domain.user.exception.UserLogoutFailException;
import com.raining.simple_planner.domain.user.exception.UserNickNameDupException;
import com.raining.simple_planner.domain.user.service.UserCommandService;
import com.raining.simple_planner.domain.user.service.UserQueryService;
import com.raining.simple_planner.global.jwt.dto.TokenInfo;
import com.raining.simple_planner.global.jwt.service.JWTTokenService;
import com.raining.simple_planner.global.result.ResultCode;
import com.raining.simple_planner.global.result.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final JWTTokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<ResultResponse> postMethodName(@RequestBody UserRegisterDTO userRegisterDTO) {
        if (userQueryService.isIdExists(userRegisterDTO.getId())) {
            throw new UserIdDupException();
        }
        if (userQueryService.isNickNameExists(userRegisterDTO.getNickName())) {
            throw new UserNickNameDupException();
        }
        userCommandService.registerUser(userRegisterDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_REGISTRATION_SUCCESS));
    }

    @PostMapping("/login")
    public ResponseEntity<ResultResponse> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        // 로그인 로직 구현
        TokenInfo tokenInfo = tokenService.login(loginRequestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_LOGIN_SUCCESS, tokenInfo));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResultResponse> logout(@RequestHeader("Authorization") String authorization) {
        
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // 토큰 정보 추출
            String token = authorization.substring(7);

            // 로그아웃 처리
            tokenService.logout(token);
        } else {
            // 잘못된 토큰 형식 처리 (예: 예외 던지기)
            throw new UserLogoutFailException();
        }
        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_LOGOUT_SUCCESS));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResultResponse> tokenRefresh(@RequestBody TokenRefreshRequestDTO requestDTO) {

        TokenInfo tokenInfo =tokenService.refreshToken(requestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.TOKEN_REFRESH_SUCCESS, tokenInfo));
    }
    
}
