package com.raining.simple_planner.domain.group.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class GroupNotFoundException extends BusinessException {

    public GroupNotFoundException() {
        super(ExceptionCode.GROUP_NOT_FOUND);
    }
    
}
