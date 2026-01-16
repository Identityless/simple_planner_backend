package com.raining.simple_planner.domain.plan.record;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class TimeTable {

    private final List<TimeSlot> timeSlots;

    protected TimeTable(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots != null ? timeSlots : new ArrayList<>();
    }

    public static TimeTable empty() {
        return new TimeTable(new ArrayList<>());
    }
}

