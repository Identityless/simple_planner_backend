package com.raining.simple_planner.domain.plan.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raining.simple_planner.domain.group.service.GroupQueryService;
import com.raining.simple_planner.domain.plan.document.Plan;
import com.raining.simple_planner.domain.plan.dto.PlanAddDateInfoRequestDTO;
import com.raining.simple_planner.domain.plan.dto.PlanRegistrationRequestDTO;
import com.raining.simple_planner.domain.plan.dto.PlanUpdateRequestDTO;
import com.raining.simple_planner.domain.plan.exception.PlanNoPermissionException;
import com.raining.simple_planner.domain.plan.repository.PlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanCommandService {

    private final PlanRepository planRepository;
    private final GroupQueryService groupQueryService;

    /**
     * 플랜 등록
     * @param requestDTO
     * @return
     */
    @Transactional
    public String register(PlanRegistrationRequestDTO requestDTO, String userLoginId) {

        // 그룹장인지 확인
        if (!groupQueryService.isGroupOwner(requestDTO.groupId(), userLoginId)) {
            throw new PlanNoPermissionException();
        }
        // 등록 시점 그룹 멤버 목록을 기준으로 플랜 초기화
        List<String> groupMembers = groupQueryService.findGroupUserList(requestDTO.groupId());
        
        Plan plan = Plan.initPlan(requestDTO, groupMembers);
        plan = planRepository.save(plan);
        
        log.info("플랜 등록 | Plan ID : {}, Group ID : {}", plan.getId(), requestDTO.groupId());
        
        return plan.getId();
    }

    /**
     * 플랜 수정
     * @param requestDTO
     * @param userLoginId
     */
    @Transactional
    public void update(PlanUpdateRequestDTO requestDTO, String userLoginId) {
        Plan plan = planRepository.findById(requestDTO.id()).orElseThrow(PlanNoPermissionException::new);
        
        // 그룹장인지 확인
        if (!groupQueryService.isGroupOwner(plan.getGroupId(), userLoginId)) {
            throw new PlanNoPermissionException();
        }

        // 플랜 수정
        plan.updatePlan(requestDTO);

        log.info("플랜 수정 | Plan ID : {}, Group ID : {}", plan.getId(), plan.getGroupId());
    }

    /**
     * 플랜 날짜-시간표 정보 전체 초기화
     * @param planId
     * @param userLoginId
     */
    @Transactional
    public void resetDateTable(String planId, String userLoginId) {
        Plan plan = planRepository.findById(planId).orElseThrow(PlanNoPermissionException::new);
        
        // 그룹장인지 확인
        if (!groupQueryService.isGroupOwner(plan.getGroupId(), userLoginId)) {
            throw new PlanNoPermissionException();
        }

        // 날짜-시간표 정보 초기화
        plan.clearDateTable();

        log.info("플랜 날짜-시간표 정보 초기화 | Plan ID : {}, Group ID : {}", plan.getId(), plan.getGroupId());
    }

    /**
     * 플랜 개인 날짜-시간표 정보 초기화
     * @param planId
     * @param userLoginId
     */
    @Transactional
    public void resetPersonalDateTable(String planId, String userLoginId) {
        Plan plan = planRepository.findById(planId).orElseThrow(PlanNoPermissionException::new);

        // 그룹 멤버인지 확인
        List<String> groupMembers = groupQueryService.findGroupUserList(plan.getGroupId());
        if (!groupMembers.contains(userLoginId)) {
            throw new PlanNoPermissionException();
        }

        // 개인 날짜-시간표 정보 초기화
        plan.getDateTables().remove(userLoginId);

        log.info("플랜 개인 날짜-시간표 정보 초기화 | Plan ID : {}, User ID : {}", plan.getId(), userLoginId);
    }

    /**
     * 플랜 날짜-시간표 정보 추가
     * @param requestDTO
     * @param userLoginId
     */
    @Transactional
    public void addDateInfo(PlanAddDateInfoRequestDTO requestDTO, String userLoginId) {
        Plan plan = planRepository.findById(requestDTO.planId()).orElseThrow(PlanNoPermissionException::new);

        // 그룹 멤버인지 확인
        List<String> groupMembers = groupQueryService.findGroupUserList(plan.getGroupId());
        if (!groupMembers.contains(userLoginId)) {
            throw new PlanNoPermissionException();
        }

        // 날짜-시간표 정보 추가
        plan.addDateInfo(userLoginId, requestDTO);

        log.info("플랜 날짜-시간표 정보 추가 | Plan ID : {}, User ID : {}", plan.getId(), userLoginId);
    }
}
