package com.raining.simple_planner.domain.plan.document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.raining.simple_planner.domain.plan.constant.PlanMode;
import com.raining.simple_planner.domain.plan.constant.TimeTableMode;
import com.raining.simple_planner.domain.plan.dto.PlanAddDateInfoRequestDTO;
import com.raining.simple_planner.domain.plan.dto.PlanRegistrationRequestDTO;
import com.raining.simple_planner.domain.plan.dto.PlanUpdateRequestDTO;
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
    private  PlanMode planMode;                    // 플랜 모드(참석 가능일 지정 / 참석 불가일 지정)
    private  TimeTableMode timeTableMode;          // 시간표 모드(날짜만 / 날짜+시간)
    private final Map<String, DateTable> dateTables;    // 사용자 ID 별 날짜-시간표 기록

    protected Plan(
            String groupId,
            String title,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime deadline,
            PlanMode planMode,
            TimeTableMode timeTableMode,
            Map<String, DateTable> dateTables
    ) {
        this.groupId = groupId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deadline = deadline;
        this.planMode = planMode;
        this.timeTableMode = timeTableMode;
        this.dateTables = dateTables != null ? dateTables : new HashMap<>();
    }

    /**
     * 플랜 정보 업데이트
     * @param requestDTO
     */
    public void updatePlan(PlanUpdateRequestDTO requestDTO) {
        // 플랜 모드 또는 시간표 모드가 변경될 경우 날짜-시간표 정보 초기화
        if (this.timeTableMode != TimeTableMode.fromCode(requestDTO.timeTableMode())
            || this.planMode != PlanMode.fromCode(requestDTO.planMode())) {
            clearDateTable();
        }
        this.title = requestDTO.title();
        this.description = requestDTO.description();
        this.startDate = LocalDateTime.parse(requestDTO.startDate());
        this.endDate = LocalDateTime.parse(requestDTO.endDate());
        this.deadline = LocalDateTime.parse(requestDTO.deadline());
        this.planMode = PlanMode.fromCode(requestDTO.planMode());
        this.timeTableMode = TimeTableMode.fromCode(requestDTO.timeTableMode());
    }

    /**
     * 유저-시간표 정보 추가
     * @param userLoginId
     * @param requestDTO
     */
    public void addDateInfo(String userLoginId, PlanAddDateInfoRequestDTO requestDTO) {
        DateTable dateTable = this.dateTables.get(userLoginId);
        // 해당 사용자의 날짜-시간표가 있을 경우에만 추가
        if (dateTable != null) {
            dateTable.addTimeInfo(requestDTO, this.timeTableMode);
        }
        else { // 없을 경우 새로 생성 후 추가
            dateTable = DateTable.empty();
            dateTable.addTimeInfo(requestDTO, this.timeTableMode);
            this.dateTables.put(userLoginId, dateTable);
        }
    }

    /**
     * 유저-시간표 정보 초기화
     */
    public void clearDateTable() {
        this.dateTables.clear();
    }

    /**
     * 새 플랜 생성
     * @param requestDTO
     * @param memberLoginIds
     * @return
     */
    public static Plan initPlan(PlanRegistrationRequestDTO requestDTO, List<String> memberLoginIds) {
        Map<String, DateTable> tables = new HashMap<>();
        for (String memberLoginId : memberLoginIds) {
            tables.put(memberLoginId, DateTable.empty());
        }

        return new Plan(
                requestDTO.groupId(),
                requestDTO.title(),
                requestDTO.description(),
                LocalDateTime.parse(requestDTO.startDate()),
                LocalDateTime.parse(requestDTO.endDate()),
                LocalDateTime.parse(requestDTO.deadline()),
                PlanMode.fromCode(requestDTO.planMode()),
                TimeTableMode.fromCode(requestDTO.timeTableMode()),
                tables
        );
    }
}

