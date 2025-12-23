package com.raining.simple_planner.domain.group.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class GroupAlreadyInvitedException extends BusinessException {
    public GroupAlreadyInvitedException() {
        super(ExceptionCode.GROUP_ALREADY_INVITED);
    }
}
