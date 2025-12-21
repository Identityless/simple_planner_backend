package com.raining.simple_planner.domain.user.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ExceptionCode.USER_NOT_FOUND);
    }
    
}
