package com.raining.simple_planner.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_USER("ROLE_USER", "일반 사용자")
    ;
    private final String roleName;
    private final String description;
}
