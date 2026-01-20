package com.raining.simple_planner.domain.plan.record;

import java.time.LocalTime;

import com.raining.simple_planner.global.util.TimeUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot {

    private String startTime;
    private String endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = TimeUtil.localTimeToString(startTime);
        this.endTime = TimeUtil.localTimeToString(endTime);
    }

    protected TimeSlot(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return TimeUtil.stringToLocalTime(startTime);
    }

    public LocalTime getEndTime() {
        return TimeUtil.stringToLocalTime(endTime);
    }
}
