package com.raining.simple_planner.domain.plan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raining.simple_planner.domain.group.service.GroupCommandService;
import com.raining.simple_planner.domain.plan.dto.PlanAddDateInfoRequestDTO;
import com.raining.simple_planner.domain.plan.dto.PlanDeleteRequestDTO;
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

    /**
     * 플랜 전체 날짜-시간표 정보 초기화
     * @param planId
     * @param authorization
     * @return
     */
    @PutMapping("/reset/{planId}")
    public ResponseEntity<ResultResponse> resetDateTable(
        @PathVariable String planId,
        @RequestHeader("Authorization") String authorization
    ) {
        // 사용자 로그인 ID 추출
        String userLoginId = TokenUtil.getUserLoginId(authorization);

        planCommandService.resetDateTable(planId, userLoginId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PLAN_DATE_TABLE_RESET_SUCCESS));
    }

    /**
     * 플랜 개인 날짜-시간표 정보 초기화
     * @param planId
     * @param authorization
     * @return
     */
    @PutMapping("/reset/personal/{planId}")
    public ResponseEntity<ResultResponse> resetPersonalDateTable(
        @PathVariable String planId,
        @RequestHeader("Authorization") String authorization
    ) {
        // 사용자 로그인 ID 추출
        String userLoginId = TokenUtil.getUserLoginId(authorization);

        planCommandService.resetPersonalDateTable(planId, userLoginId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PLAN_PERSONAL_DATE_TABLE_RESET_SUCCESS));
    }

    /**
     * 플랜 날짜-시간표 정보 추가
     * @param requestDTO
     * @param authorization
     * @return
     */
    @PostMapping("/addDateInfo")
    public ResponseEntity<ResultResponse> addDateInfo(
        @RequestBody PlanAddDateInfoRequestDTO requestDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        // 사용자 로그인 ID 추출
        String userLoginId = TokenUtil.getUserLoginId(authorization);

        planCommandService.addDateInfo(requestDTO, userLoginId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PLAN_DATE_INFO_ADD_SUCCESS));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse> deletePlan(
        @RequestBody PlanDeleteRequestDTO requestDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        // 사용자 로그인 ID 추출
        String userLoginId = TokenUtil.getUserLoginId(authorization);
        
        // 플랜 삭제
        planCommandService.deletePlan(requestDTO, userLoginId);

        // 그룹에서 플랜 ID 제거
        groupCommandService.removePlan(requestDTO, userLoginId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.PLAN_DELETE_SUCCESS));
    }
}