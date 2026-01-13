package com.raining.simple_planner.domain.group.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raining.simple_planner.domain.group.dto.GroupInfoUpdateRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupListResponseDTO;
import com.raining.simple_planner.domain.group.dto.GroupOwnerChangeRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupRegistrationRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupUserInviteActionRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupUserInviteRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupUserRemoveRequestDTO;
import com.raining.simple_planner.domain.group.service.GroupCommandService;
import com.raining.simple_planner.domain.group.service.GroupQueryService;
import com.raining.simple_planner.domain.user.dto.UserGroupUpdateDTO;
import com.raining.simple_planner.domain.user.service.UserCommandService;
import com.raining.simple_planner.global.result.ResultCode;
import com.raining.simple_planner.global.result.ResultResponse;
import com.raining.simple_planner.global.util.TokenUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupQueryService groupQueryService;
    private final GroupCommandService groupCommandService;
    private final UserCommandService userCommandService;

    /**
     * 그룹 등록
     * @param groupRegistrationRequestDTO
     * @param authorization
     * @return
     */
    @PostMapping("/registration")
    public ResponseEntity<ResultResponse> registration(
            @RequestBody GroupRegistrationRequestDTO groupRegistrationRequestDTO,
            @RequestHeader("Authorization") String authorization
    ) {
        String userLoginId = TokenUtil.getUserLoginId(authorization);

        String groupId = groupCommandService.registration(userLoginId, groupRegistrationRequestDTO);

        userCommandService.addUserGroup(new UserGroupUpdateDTO(userLoginId, groupId));

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_REGISTRATION_SUCCESS));
    }

    /**
     * 그룹 정보 수정
     * @param groupInfoUpdateRequestDTO
     * @param authorization
     * @return
     */
    @PutMapping("/update")
    public ResponseEntity<ResultResponse> putMethodName(
            @RequestBody GroupInfoUpdateRequestDTO groupInfoUpdateRequestDTO,
            @RequestHeader("Authorization") String authorization
    ) { 
        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        groupCommandService.update(UserLoginId, groupInfoUpdateRequestDTO);
        
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_UPDATE_SUCCESS));
    }

    /**
     * 그룹에 유저 초대
     * @param groupUserInviteRequestDTO
     * @param authorization
     * @return
     */
    @PostMapping("/invite")
    public ResponseEntity<ResultResponse> inviteUsers(
            @RequestBody GroupUserInviteRequestDTO groupUserInviteRequestDTO,
            @RequestHeader("Authorization") String authorization
    ) {
        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        groupCommandService.invite(UserLoginId, groupUserInviteRequestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_INVITE_SUCCESS));
    }

    /**
     * 그룹 초대 수락
     * @param groupUserInviteActionRequestDTO
     * @param authorization
     * @return
     */
    @PutMapping("invite/accept")
    public ResponseEntity<ResultResponse> inviteAccept(
        @RequestBody GroupUserInviteActionRequestDTO groupUserInviteActionRequestDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        String groupId = groupCommandService.inviteAccept(UserLoginId, groupUserInviteActionRequestDTO);

        userCommandService.addUserGroup(new UserGroupUpdateDTO(UserLoginId, groupId));
        
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_USER_ADD_SUCCESS));
    }

    /**
     * 그룹 초대 거절
     * @param groupUserInviteActionRequestDTO
     * @param authorization
     * @return
     */
    @PutMapping("invite/deny")
    public ResponseEntity<ResultResponse> inviteDeny(
        @RequestBody GroupUserInviteActionRequestDTO groupUserInviteActionRequestDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        groupCommandService.inviteDeny(UserLoginId, groupUserInviteActionRequestDTO);
        
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_INVITE_DENY_SUCCESS));
    }
    
    /**
     * 그룹에서 유저 추방
     * @param groupUserRemoveRequestDTO
     * @param authorization
     * @return
     */
    @PutMapping("/removeUser")
    public ResponseEntity<ResultResponse> removeUser(
        @RequestBody GroupUserRemoveRequestDTO groupUserRemoveRequestDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        String groupId = groupCommandService.removeUser(UserLoginId, groupUserRemoveRequestDTO);

        for (String removeUserId : groupUserRemoveRequestDTO.getRemoveUserIds()) {
            userCommandService.deleteUserGroup(new UserGroupUpdateDTO(removeUserId, groupId));
        }

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_USER_REMOVE_SUCCESS));
    }
    
    @GetMapping("/list")
    public ResponseEntity<ResultResponse> findListByUser(
        @RequestHeader("Authorization") String authorization
    ) {
        String userLoginId = TokenUtil.getUserLoginId(authorization);

        GroupListResponseDTO response = groupQueryService.findUserGroupList(userLoginId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_LIST_FIND_SUCCESS, response));
    }

    @PutMapping("/changeOwner")
    public ResponseEntity<ResultResponse> changeOwner(
        @RequestBody GroupOwnerChangeRequestDTO groupOwnerChangeRequestDTO,
        @RequestHeader("Authorization") String authorization
    ) {
        String userLoginId = TokenUtil.getUserLoginId(authorization);

        groupCommandService.changeOwner(userLoginId, groupOwnerChangeRequestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GROUP_UPDATE_SUCCESS));
    }

}
