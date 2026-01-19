package com.raining.simple_planner.domain.plan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raining.simple_planner.domain.group.service.GroupCommandService;
import com.raining.simple_planner.domain.plan.dto.PlanRegistrationRequestDTO;
import com.raining.simple_planner.domain.plan.dto.PlanUpdateRequestDTO;
import com.raining.simple_planner.domain.plan.service.PlanCommandService;
import com.raining.simple_planner.domain.plan.service.PlanQueryService;
import com.raining.simple_planner.global.result.ResultCode;
import com.raining.simple_planner.global.result.ResultResponse;
import com.raining.simple_planner.global.util.TokenUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanQueryService planQueryService;
    private final PlanCommandService planCommandService;
    private final GroupCommandService groupCommandService;

    /**
     * 플랜 등록
     * @param requestDTO
     * @param authorization
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<ResultResponse> register(
        @RequestBody PlanRegistrationRequestDTO requestDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        // 사용자 로그인 ID 추출
        String userLoginId = TokenUtil.getUserLoginId(authorization);

        // 플랜 등록 서비스 호출
        String planId = planCommandService.register(requestDTO , userLoginId);
        // 그룹에 플랜 ID 추가 서비스 호출
        groupCommandService.addPlan(requestDTO.groupId(), planId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PLAN_REGISTRATION_SUCCESS));
    }

    /**
     * 플랜 수정
     * @param requestDTO
     * @param authorization
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<ResultResponse> update(
        @RequestBody PlanUpdateRequestDTO requestDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        // 사용자 로그인 ID 추출
        String userLoginId = TokenUtil.getUserLoginId(authorization);

        planCommandService.update(requestDTO, userLoginId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PLAN_UPDATE_SUCCESS));
    }
}
