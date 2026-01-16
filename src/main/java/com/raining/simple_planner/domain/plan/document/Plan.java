package com.raining.simple_planner.domain.plan.document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.raining.simple_planner.domain.plan.constant.PlanMode;
import com.raining.simple_planner.domain.plan.constant.TimeTableMode;
import com.raining.simple_planner.domain.plan.record.DateTable;
import com.raining.simple_planner.global.document.BaseDocument;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "plan")
public class Plan extends BaseDocument {

    private final String groupId;                  // 소속 그룹 ID
    private  String title;                         // 플랜 제목
    private  String description;                   // 플랜 설명
    private  LocalDateTime startDate;              // 플랜 시작일
    private  LocalDateTime endDate;                // 플랜 종료일
    private  LocalDateTime deadline;               // 플랜 응답 마감일
    private  PlanMode mode;                        // 플랜 모드(참석 가능일 지정 / 참석 불가일 지정)
    private  TimeTableMode timeTableMode;          // 시간표 모드(날짜만 / 날짜+시간)
    private final Map<String, DateTable> dateTables;    // 사용자 ID 별 날짜-시간표 기록

    protected Plan(
            String groupId,
            String title,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime deadline,
            PlanMode mode,
            TimeTableMode timeTableMode,
            Map<String, DateTable> dateTables
    ) {
        this.groupId = groupId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deadline = deadline;
        this.mode = mode;
        this.timeTableMode = timeTableMode;
        this.dateTables = dateTables != null ? dateTables : new HashMap<>();
    }

    public static Plan initPlan(
            String groupId,
            String title,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime deadline,
            PlanMode mode,
            TimeTableMode timeTableMode,
            List<String> memberIds
    ) {
        Map<String, DateTable> tables = new HashMap<>();
        for (String memberId : memberIds) {
            tables.put(memberId, DateTable.empty());
        }

        return new Plan(
                groupId,
                title,
                description,
                startDate,
                endDate,
                deadline,
                mode,
                timeTableMode,
                tables
        );
    }
}

