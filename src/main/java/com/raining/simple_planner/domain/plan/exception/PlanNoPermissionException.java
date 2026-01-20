package com.raining.simple_planner.domain.plan.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class PlanNoPermissionException extends BusinessException {
    public PlanNoPermissionException() {
        super(ExceptionCode.PLAN_NO_PERMISSION);
    }
    
}
