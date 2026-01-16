package com.raining.simple_planner.domain.plan.controller;

import org.springframework.web.bind.annotation.RestController;

import com.raining.simple_planner.domain.plan.service.PlanCommandService;
import com.raining.simple_planner.domain.plan.service.PlanQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlanController {
    private final PlanQueryService planQueryService;
    private final PlanCommandService planCommandService;
}
