package com.raining.simple_planner.domain.group.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class GroupNoPermissionException extends BusinessException{

    public GroupNoPermissionException() {
        super(ExceptionCode.GROUP_NO_PERMISSION);
    }
    
}
