package com.raining.simple_planner.domain.group.exception;

import com.raining.simple_planner.global.exception.BusinessException;
import com.raining.simple_planner.global.exception.ExceptionCode;

public class GroupInvitationQueueNotFoundException extends BusinessException {
    public GroupInvitationQueueNotFoundException () {
        super(ExceptionCode.GROUP_INVITATION_QUEUE_NOT_FOUND);
    }
}
