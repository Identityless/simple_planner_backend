package com.raining.simple_planner.domain.user.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class UserNickNameDupException extends BusinessException {
    public UserNickNameDupException() {
        super(ExceptionCode.USER_NICKNAME_DUPLICATED);
    }
    
}
