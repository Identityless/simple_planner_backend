package com.raining.simple_planner.domain.group.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raining.simple_planner.domain.group.dto.GroupInfoUpdateRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupRegistrationRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupUserInviteRequestDTO;
import com.raining.simple_planner.domain.group.service.GroupCommandService;
import com.raining.simple_planner.global.result.ResultCode;
import com.raining.simple_planner.global.result.ResultResponse;
import com.raining.simple_planner.global.util.TokenUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;



@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
    private final GroupCommandService groupCommandService;

    @PostMapping("/registration")
    public ResponseEntity<ResultResponse> registration(
            @RequestBody GroupRegistrationRequestDTO groupRegistrationRequestDTO,
            @RequestHeader("Authorization") String authorization
        ) {

        String userId = TokenUtil.getUserId(authorization);

        groupCommandService.registration(userId, groupRegistrationRequestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_REGISTRATION_SUCCESS));
    }

    @PutMapping("/update")
    public ResponseEntity<ResultResponse> putMethodName(
            @RequestBody GroupInfoUpdateRequestDTO groupInfoUpdateRequestDTO,
            @RequestHeader("Authorization") String authorization
        ) { 
        
        String userId = TokenUtil.getUserId(authorization);

        groupCommandService.update(userId, groupInfoUpdateRequestDTO);
        
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_UPDATE_SUCCESS));
    }

    @PostMapping("/invite")
    public ResponseEntity<ResultResponse> inviteUsers(
            @RequestBody GroupUserInviteRequestDTO groupUserInviteRequestDTO,
            @RequestHeader("Authorization") String authorization
    ) {
        String userId = TokenUtil.getUserId(authorization);

        groupCommandService.invite(userId, groupUserInviteRequestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_INVITE_SUCCESS));
    }
    
    
}
