package com.raining.simple_planner.domain.user.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class UserNoPermissionException extends BusinessException {
    public UserNoPermissionException () {
        super(ExceptionCode.USER_NO_PERMISSION);
    }
}
