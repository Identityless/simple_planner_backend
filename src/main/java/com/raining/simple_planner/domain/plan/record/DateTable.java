package com.raining.simple_planner.domain.plan.record;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class DateTable {

    private final Map<String, TimeTable> timeTables;

    // 생성자 1개
    protected DateTable(Map<String, TimeTable> timeTables) {
        this.timeTables = timeTables != null ? timeTables : new HashMap<>();
    }

    public static DateTable empty() {
        return new DateTable(new HashMap<>());
    }
}

