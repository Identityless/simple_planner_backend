package com.raining.simple_planner.domain.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raining.simple_planner.domain.user.document.User;
import com.raining.simple_planner.domain.user.dto.ChangePasswordRequestDTO;
import com.raining.simple_planner.domain.user.dto.FriendAddRequestDTO;
import com.raining.simple_planner.domain.user.dto.FriendDeleteRequestDTO;
import com.raining.simple_planner.domain.user.dto.FriendListResponseDTO;
import com.raining.simple_planner.domain.user.dto.UserInfoResponseDTO;
import com.raining.simple_planner.domain.user.dto.UserInfoUpdateRequestDTO;
import com.raining.simple_planner.domain.user.service.UserCommandService;
import com.raining.simple_planner.domain.user.service.UserQueryService;
import com.raining.simple_planner.global.result.ResultCode;
import com.raining.simple_planner.global.result.ResultResponse;
import com.raining.simple_planner.global.util.TokenUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @GetMapping("/find")
    public ResponseEntity<ResultResponse> getMethodName(
        @RequestParam String type, 
        @RequestParam String value,
        @RequestHeader("Authorization") String authorization) {

        User user = userQueryService.getUserByLoginId(TokenUtil.getUserLoginId(authorization));

        boolean isOwn = user.getId().equals(value) || user.getUserTag().equals(value);
        
        UserInfoResponseDTO responseDTO = userQueryService.findUser(type, value, isOwn);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_FIND_SUCCESS, responseDTO));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<ResultResponse> changePassword(
        @RequestHeader("Authorization") String authorization,
        @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {

        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        userCommandService.changePassword(UserLoginId, changePasswordRequestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_PASSWORD_CHANGE_SUCCESS));
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<ResultResponse> updateUserInfo(
        @RequestHeader("Authorization") String authorization,
        @RequestBody UserInfoUpdateRequestDTO userInfoUpdateRequestDTO) {

        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        userCommandService.updateUserInfo(UserLoginId, userInfoUpdateRequestDTO);

        UserInfoResponseDTO updatedUserInfo = userQueryService.findUser("id", UserLoginId, true);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_UPDATE_SUCCESS, updatedUserInfo));
    }

    @PostMapping("/friend/request")
    public ResponseEntity<ResultResponse> postMethodName(@RequestBody FriendAddRequestDTO requestDTO) {

        userCommandService.saveFriendAddRequest(requestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.FRIEND_REQUEST_SUCCESS));
    }

    @PostMapping("/friend/accept")
    public ResponseEntity<ResultResponse> acceptFriendRequest(
        @RequestHeader("Authorization") String authorization,
        @RequestBody String requestId
    ) {
        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        userCommandService.acceptFriendRequest(UserLoginId, requestId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.FRIEND_REQUEST_ACCEPT_SUCCESS));
    }

    @PostMapping("/friend/deny")
    public ResponseEntity<ResultResponse> denyFriendRequest(
        @RequestHeader("Authorization") String authorization,
        @RequestBody String requestId
    ) {
        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        userCommandService.denyFriendRequest(UserLoginId, requestId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.FRIEND_REQUEST_DENY_SUCCESS));
    }

    @GetMapping("/friend/list")
    public ResponseEntity<ResultResponse> getFriendList(
        @RequestHeader("Authorization") String authorization) {

        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        FriendListResponseDTO friendList = userQueryService.getFriendList(UserLoginId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.FRIEND_LIST_REQUEST_SUCCESS, friendList));
    }

    @PutMapping("/friend/delete")
    public ResponseEntity<ResultResponse> deleteFriends(
        @RequestHeader("Authorization") String authorization,
        @RequestBody FriendDeleteRequestDTO requestDTO) {

        String UserLoginId = TokenUtil.getUserLoginId(authorization);

        userCommandService.deleteFriends(UserLoginId, requestDTO);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.FRIEND_DELETE_SUCCESS));
    }
}