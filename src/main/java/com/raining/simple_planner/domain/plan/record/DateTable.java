package com.raining.simple_planner.domain.plan.record;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.raining.simple_planner.domain.plan.constant.TimeTableMode;
import com.raining.simple_planner.domain.plan.dto.PlanAddDateInfoRequestDTO;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DateTable {

    private Map<LocalDate, TimeTable> timeTables;

    public DateTable(Map<LocalDate, TimeTable> timeTables) {
        this.timeTables = timeTables != null ? timeTables : new HashMap<>();
    }

    public void addTimeInfo(PlanAddDateInfoRequestDTO requestDTO, TimeTableMode mode) {
        LocalDate date = requestDTO.from().toLocalDate();
        TimeTable timeTable = this.timeTables.get(date);
        // 시간표가 없을 경우 새로 생성
        if (timeTable == null) {
            timeTable = TimeTable.empty();
            // ONLY_DATE 모드일 경우 시간 정보 없이 추가
            if (mode == TimeTableMode.ONLY_DATE) {
                this.timeTables.put(date, timeTable);
                return;
            }
            // DATE_TIME 모드일 경우 시간 정보 추가
            timeTable.addTimeSlot(requestDTO.from().toLocalTime(), requestDTO.to().toLocalTime());
            this.timeTables.put(date, timeTable);
        }
        else {
            // 기존 시간표에 시간 정보 추가
            if (mode == TimeTableMode.DATE_TIME) {
                timeTable.addTimeSlot(requestDTO.from().toLocalTime(), requestDTO.to().toLocalTime());
            }
        }
    }

    public static DateTable empty() {
        return new DateTable(new HashMap<>());
    }
}

