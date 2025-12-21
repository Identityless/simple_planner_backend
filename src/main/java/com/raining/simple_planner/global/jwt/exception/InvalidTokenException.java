package com.raining.simple_planner.global.jwt.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(ExceptionCode.INVALID_TOKEN);
    }
    
}
