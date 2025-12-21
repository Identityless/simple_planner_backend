package com.raining.simple_planner.domain.user.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class UserIdDupException extends BusinessException {
    public UserIdDupException() {
        super(ExceptionCode.USER_ACCOUNT_DUPLICATED);
    }
    
}
