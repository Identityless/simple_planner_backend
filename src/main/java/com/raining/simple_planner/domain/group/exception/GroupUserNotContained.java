package com.raining.simple_planner.domain.group.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class GroupUserNotContained extends BusinessException {
    public GroupUserNotContained() {
        super(ExceptionCode.GROUP_USER_NOT_CONTAINED);
    }
}
