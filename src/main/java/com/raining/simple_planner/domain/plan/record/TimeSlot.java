package com.raining.simple_planner.domain.plan.record;

import java.time.LocalTime;

import lombok.Getter;

@Getter
public class TimeSlot {

    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
