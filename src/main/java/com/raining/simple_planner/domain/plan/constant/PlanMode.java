package com.raining.simple_planner.domain.plan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanMode {
    AVAILABLE("available", "참석 가능일 지정"),
    UNAVAILABLE("unavailable", "참석 불가일 지정");

    private final String code;
    private final String description;
}
