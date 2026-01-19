package com.raining.simple_planner.domain.plan.dto;

public record PlanUpdateRequestDTO(
    String id,
    String title,
    String description,
    String startDate,
    String endDate,
    String deadline,
    String planMode,
    String timeTableMode,
    boolean reset
) {
    
}
