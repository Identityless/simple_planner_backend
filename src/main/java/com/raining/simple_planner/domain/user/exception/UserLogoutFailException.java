package com.raining.simple_planner.domain.user.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class UserLogoutFailException extends BusinessException {
    public UserLogoutFailException() {
        super(ExceptionCode.USER_LOGOUT_FAIL);
    }
}
