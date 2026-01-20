package com.raining.simple_planner.domain.plan.dto;

import java.time.LocalDateTime;

public record PlanAddDateInfoRequestDTO(
    String planId,
    LocalDateTime from,
    LocalDateTime to
) {
    
}
