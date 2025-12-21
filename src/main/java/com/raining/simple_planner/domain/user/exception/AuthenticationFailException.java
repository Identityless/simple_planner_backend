package com.raining.simple_planner.domain.user.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class AuthenticationFailException extends BusinessException {
    public AuthenticationFailException() {
        super(ExceptionCode.USER_AUTH_FAIL);
    }
    
}
