package com.raining.simple_planner.global.jwt.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class NoAuthInfoException extends BusinessException {
    public NoAuthInfoException() {
        super(ExceptionCode.UN_AUTHORIZED_ACCESS);
    }
}
