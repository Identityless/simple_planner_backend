package com.raining.simple_planner.domain.plan.record;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeTable {

    private List<TimeSlot> timeSlots;

    public TimeTable(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots != null ? timeSlots : new ArrayList<>();
    }

    public void addTimeSlot(LocalTime from, LocalTime to) {
        this.timeSlots.add(new TimeSlot(from, to));
    }

    public static TimeTable empty() {
        return new TimeTable(new ArrayList<>());
    }
}

