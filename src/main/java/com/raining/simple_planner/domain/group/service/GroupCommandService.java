package com.raining.simple_planner.domain.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raining.simple_planner.domain.group.document.Group;
import com.raining.simple_planner.domain.group.document.GroupInvitationQueue;
import com.raining.simple_planner.domain.group.dto.GroupInfoUpdateRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupOwnerChangeRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupRegistrationRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupUserInviteActionRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupUserInviteRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupUserRemoveRequestDTO;
import com.raining.simple_planner.domain.group.exception.GroupNotFoundException;
import com.raining.simple_planner.domain.group.exception.GroupUserNotContainedException;
import com.raining.simple_planner.domain.group.exception.GroupAlreadyInvitedException;
import com.raining.simple_planner.domain.group.exception.GroupInvitationQueueNotFoundException;
import com.raining.simple_planner.domain.group.exception.GroupNoPermissionException;
import com.raining.simple_planner.domain.group.repository.GroupInvitationQueueRepository;
import com.raining.simple_planner.domain.group.repository.GroupRepository;
import com.raining.simple_planner.domain.user.document.User;
import com.raining.simple_planner.domain.user.service.UserQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupCommandService {
    private final UserQueryService userQueryService;
    private final GroupRepository groupRepository;
    private final GroupInvitationQueueRepository groupInvitationQueueRepository;

    /**
     * 그룹 등록
     * @param userId
     * @param requestDTO
     */
    @Transactional
    public String registration(String userLoginId, GroupRegistrationRequestDTO requestDTO) {
        Group group = Group.builder()
                .name(requestDTO.getName())
                .ownerId(userLoginId)
                .description(requestDTO.getDescription())
                .memberIds(List.of(userLoginId))
                .planIds(List.of())
                .whiteBoardContentIds(List.of())
                .build();
        
        group = groupRepository.save(group);

        log.info("그룹 생성 | Group Owner : {}, Group Name : {}", userLoginId, requestDTO.getName());

        return group.getId();
    }

    /**
     * 그룹 정보 수정
     * @param userLoginId
     * @param groupInfoUpdateRequestDTO
     */
    @Transactional
    public void update(String userLoginId, GroupInfoUpdateRequestDTO groupInfoUpdateRequestDTO) {
        Group group = groupRepository.findById(groupInfoUpdateRequestDTO.getGroupId())
                .orElseThrow(GroupNotFoundException::new);

        // 그루비름 수정
        if (groupInfoUpdateRequestDTO.getName() != null
                && !groupInfoUpdateRequestDTO.getName().equals("")) {
            group.setName(groupInfoUpdateRequestDTO.getName());
        }

        // 그룹 설명 수정
        if (groupInfoUpdateRequestDTO.getDescription() != null
                && !groupInfoUpdateRequestDTO.getDescription().equals("")) {
            group.setDescription(groupInfoUpdateRequestDTO.getDescription());
        }

        // 그룹 주인장 변경
        if (groupInfoUpdateRequestDTO.getOwnerId() != null 
                && !groupInfoUpdateRequestDTO.getOwnerId().equals("")) {
            if (!group.getOwnerId().equals(userLoginId)) { // 그룹장과 요청 유저의 id가 일치하지 않는 경우 예외 던짐
                throw new GroupNoPermissionException();
            }
            group.setOwnerId(groupInfoUpdateRequestDTO.getOwnerId());
        }

        log.info("그룹 정보 수정 | Group ID : {}", group.getId());
    }

    /**
     * 그룹 유저 초대
     * @param userLoginId
     * @param groupUserInviteRequestDTO
     */
    @Transactional
    public void invite(String userLoginId, GroupUserInviteRequestDTO groupUserInviteRequestDTO) {
        Group group = groupRepository.findById(groupUserInviteRequestDTO.getGroupId()).orElseThrow(GroupNotFoundException::new);

        // 그룹 오너의 요청인지 체크
        if (!group.getOwnerId().equals(userLoginId)) {
            throw new GroupNoPermissionException();
        }

        List<GroupInvitationQueue> queues = new ArrayList<>();
        for (String invitedUserId : groupUserInviteRequestDTO.getInviteUserIds()) {
            // 이미 멤버이거나 초대된 내역이 있는 유저인지 확인
            if (groupInvitationQueueRepository.existsByGroupIdAndUserId(group.getId(), invitedUserId)
                || group.getMemberIds().contains(invitedUserId)) {
                throw new GroupAlreadyInvitedException();
            }
            GroupInvitationQueue groupInvitationQueue = GroupInvitationQueue.builder()
                    .groupId(group.getId())
                    .userId(invitedUserId)
                    .build();
            queues.add(groupInvitationQueue);
        }

        groupInvitationQueueRepository.saveAll(queues);

        log.info("그룹 초대 큐 저장 완료 | Group ID : {}", group.getId());
    }

    /**
     * 그룹 초대 수락
     * @param userLoginId
     * @param groupUserInviteActionRequestDTO
     */
    @Transactional
    public String inviteAccept(String userLoginId, GroupUserInviteActionRequestDTO groupUserInviteActionRequestDTO) {
        GroupInvitationQueue queue = groupInvitationQueueRepository
                .findById(groupUserInviteActionRequestDTO.getQueueId())
                .orElseThrow(GroupInvitationQueueNotFoundException::new);
        
        // 실제로 초대 내역에 있는 사용자인지 확인
        if (!queue.getUserId().equals(userLoginId)) {
            throw new GroupNoPermissionException();
        }

        // 그룹에 유저 추가
        Group group = groupRepository.findById(queue.getGroupId()).orElseThrow(GroupNotFoundException::new);
        group.getMemberIds().add(userLoginId);

        // 큐 삭제
        groupInvitationQueueRepository.delete(queue);

        log.info("그룹 유저 추가 완료 | Group ID : {}, User ID : {}", group.getId(), userLoginId);

        return group.getId();
    }

    /**
     * 그룹 초대 거절
     * @param userLoginId
     * @param groupUserInviteActionRequestDTO
     */
    @Transactional
    public void inviteDeny(String userLoginId, GroupUserInviteActionRequestDTO groupUserInviteActionRequestDTO) {
        GroupInvitationQueue queue = groupInvitationQueueRepository
                .findById(groupUserInviteActionRequestDTO.getQueueId())
                .orElseThrow(GroupInvitationQueueNotFoundException::new);

        // 실제로 초대 내역에 있는 사용자인지 확인
        if (!queue.getUserId().equals(userLoginId)) {
            throw new GroupNoPermissionException();
        }

        //큐 삭제
        groupInvitationQueueRepository.delete(queue);

        log.info("그룹 유저 초대 거부 완료 | Group ID : {}, User ID : {}", queue.getGroupId(), userLoginId);
    }

    /**
     * 그룹에서 유저 삭제
     * @param userLoginId
     * @param groupUserRemoveRequestDTO
     */
    @Transactional
    public String removeUser(String userLoginId, GroupUserRemoveRequestDTO groupUserRemoveRequestDTO) {
        Group group = groupRepository.findById(groupUserRemoveRequestDTO.getGroupId()).orElseThrow(GroupNotFoundException::new);

        // 그룹 오너의 요청인지 체크
        if (!group.getOwnerId().equals(userLoginId)) {
            throw new GroupNoPermissionException();
        }

        // 그룹 및 유저에서 매핑 정보 삭제
        for (String removeUserId : groupUserRemoveRequestDTO.getRemoveUserIds()) {
            group.getMemberIds().remove(removeUserId);
        }

        log.info("그룹 유저 삭제 완료 | Group ID : {}, User IDs : {}", group.getId(), groupUserRemoveRequestDTO.getRemoveUserIds().toString());

        return group.getId();
    }

    /**
     * 그룹장 변경
     * @param userLoginId
     * @param groupOwnerChangeRequestDTO
     */
    public void changeOwner(String userLoginId, GroupOwnerChangeRequestDTO groupOwnerChangeRequestDTO) {
        Group group = groupRepository.findById(groupOwnerChangeRequestDTO.getGroupId()).orElseThrow(GroupNotFoundException::new);
        User newOwner = userQueryService.getUserByLoginId(groupOwnerChangeRequestDTO.getNewGroupOwnerLoginId());

        // 그룹 오너의 요청인지 체크
        if (!group.getOwnerId().equals(userLoginId)) {
            throw new GroupNoPermissionException();
        }

        // 그룹에 변경 대상 유저가 속해있는지 확인
        if (!(group.getMemberIds().contains(newOwner.getLoginId()) || newOwner.getGroupKeys().contains(group.getId()))) {
            throw new GroupUserNotContainedException();
        }

        group.setOwnerId(newOwner.getLoginId());

        log.info("그룹장 변경 완료 | Group ID : {}, New Owner ID : {}", group.getId(), newOwner.getLoginId());
    }

    public List<String> deleteGroup(String userLoginId, String groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);

        // 그룹 오너의 요청인지 체크
        if (!group.getOwnerId().equals(userLoginId)) {
            throw new GroupNoPermissionException();
        }
        // 그룹 삭제
        groupRepository.delete(group);

        log.info("그룹 삭제 완료 | Group ID : {}", group.getId());

        return group.getMemberIds();
    }

}
