package com.raining.simple_planner.domain.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raining.simple_planner.domain.group.document.Group;
import com.raining.simple_planner.domain.group.document.GroupInvitationQueue;
import com.raining.simple_planner.domain.group.dto.GroupInfoUpdateRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupRegistrationRequestDTO;
import com.raining.simple_planner.domain.group.dto.GroupUserInviteRequestDTO;
import com.raining.simple_planner.domain.group.exception.GroupNotFoundException;
import com.raining.simple_planner.domain.group.exception.GroupNoPermissionException;
import com.raining.simple_planner.domain.group.repository.GroupInvitationQueueRepository;
import com.raining.simple_planner.domain.group.repository.GroupRepository;
import com.raining.simple_planner.domain.user.dto.UserGroupUpdateDTO;
import com.raining.simple_planner.domain.user.service.UserCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupCommandService {
    private final UserCommandService userCommandService;
    private final GroupRepository groupRepository;
    private final GroupInvitationQueueRepository groupInvitationQueueRepository;

    /**
     * 그룹 등록
     * @param userId
     * @param requestDTO
     */
    @Transactional
    public void registration(String userId, GroupRegistrationRequestDTO requestDTO) {
        Group group = Group.builder()
                .name(requestDTO.getName())
                .ownerId(userId)
                .description(requestDTO.getDescription())
                .memberIds(List.of(userId))
                .planIds(List.of())
                .whiteBoardContentIds(List.of())
                .build();
        
        group = groupRepository.save(group);

        log.info("그룹 생성 | Group Owner : {}, Group Name : {}", userId, requestDTO.getName());

        userCommandService.addUserGroup(new UserGroupUpdateDTO(userId, group.getId()));

    }

    /**
     * 그룹 정보 수정
     * @param userId
     * @param groupInfoUpdateRequestDTO
     */
    @Transactional
    public void update(String userId, GroupInfoUpdateRequestDTO groupInfoUpdateRequestDTO) {
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
            if (!group.getOwnerId().equals(userId)) { // 그룹장과 요청 유저의 id가 일치하지 않는 경우 예외 던짐
                throw new GroupNoPermissionException();
            }
            group.setOwnerId(groupInfoUpdateRequestDTO.getOwnerId());
        }

        log.info("그룹 정보 수정 | Group ID : {}", group.getId());
    }

    /**
     * 그룹 유저 초대
     * @param userId
     * @param groupUserInviteRequestDTO
     */
    @Transactional
    public void invite(String userId, GroupUserInviteRequestDTO groupUserInviteRequestDTO) {
        Group group = groupRepository.findById(groupUserInviteRequestDTO.getGroupId()).orElseThrow(GroupNotFoundException::new);

        // 그룹 오너의 요청인지 체크
        if (!group.getOwnerId().equals(userId)) {
            throw new GroupNoPermissionException();
        }

        List<GroupInvitationQueue> queues = new ArrayList<>();
        for (String invitedUserId : groupUserInviteRequestDTO.getInviteUserIds()) {
            GroupInvitationQueue groupInvitationQueue = GroupInvitationQueue.builder()
                    .groupId(group.getId())
                    .userId(invitedUserId)
                    .build();
            queues.add(groupInvitationQueue);
        }

        groupInvitationQueueRepository.saveAll(queues);

        log.info("그룹 초대 큐 저장 완료 | Group ID : {}", group.getId());
    }


}
