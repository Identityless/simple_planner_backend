package com.raining.simple_planner.domain.group.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class GroupOwnerChangeFailException extends BusinessException{

    public GroupOwnerChangeFailException() {
        super(ExceptionCode.GROUP_OWNER_UPDATE_FAIL);
    }
    
}
