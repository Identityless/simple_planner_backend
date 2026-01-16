package com.raining.simple_planner.domain.plan.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raining.simple_planner.domain.group.service.GroupQueryService;
import com.raining.simple_planner.domain.plan.document.Plan;
import com.raining.simple_planner.domain.plan.dto.PlanRegistrationRequestDTO;
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
}
