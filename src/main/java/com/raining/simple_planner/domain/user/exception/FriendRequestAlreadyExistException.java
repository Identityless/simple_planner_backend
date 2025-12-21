package com.raining.simple_planner.domain.user.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class FriendRequestAlreadyExistException extends BusinessException {
    public FriendRequestAlreadyExistException() {
        super(ExceptionCode.FRIEND_REQUEST_ALREADY_EXISTS);
    }
    
}
