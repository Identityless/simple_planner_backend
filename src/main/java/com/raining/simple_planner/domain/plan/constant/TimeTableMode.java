package com.raining.simple_planner.domain.plan.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeTableMode {
    ONLY_DATE("only_date", "날짜만"),
    DATE_TIME("date_time", "날짜+시간")
    ;

    private final String code;
    private final String description;

    public static TimeTableMode fromCode(String code) {
        for (TimeTableMode mode : TimeTableMode.values()) {
            if (mode.getCode().equals(code)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid TimeTableMode code: " + code);
    }
}
