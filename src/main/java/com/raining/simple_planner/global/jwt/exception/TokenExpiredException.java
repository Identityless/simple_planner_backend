package com.raining.simple_planner.global.jwt.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class TokenExpiredException extends BusinessException {
    public TokenExpiredException() {
        super(ExceptionCode.EXPIRED_TOKEN);
    }
    
}
