package com.raining.simple_planner.domain.user.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class FriendRequestNotFoundException extends BusinessException {
    public FriendRequestNotFoundException() {
        super(ExceptionCode.FRIEND_REQUEST_NOT_FOUND);
    }
    
}
