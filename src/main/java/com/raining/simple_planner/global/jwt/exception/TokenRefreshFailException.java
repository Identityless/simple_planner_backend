package com.raining.simple_planner.global.jwt.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class TokenRefreshFailException extends BusinessException {
    public TokenRefreshFailException() {
        super(ExceptionCode.TOKEN_REFRESH_FAIL);
    }
    
}
