package com.raining.simple_planner.domain.group.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raining.simple_planner.domain.group.document.Group;
import com.raining.simple_planner.domain.group.dto.GroupInfoResponseDTO;
import com.raining.simple_planner.domain.group.dto.GroupInvitationQueueInfoResponseDTO;
import com.raining.simple_planner.domain.group.dto.GroupListResponseDTO;
import com.raining.simple_planner.domain.group.exception.GroupNotFoundException;
import com.raining.simple_planner.domain.group.repository.GroupInvitationQueueRepository;
import com.raining.simple_planner.domain.group.repository.GroupRepository;
import com.raining.simple_planner.domain.user.service.UserQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupQueryService {
    private final GroupRepository groupRepository;
    private final GroupInvitationQueueRepository groupInvitationQueueRepository;
    private final UserQueryService userQueryService;


    /**
     * 유저 그룹 목록 및 그룹 초대 목록 조회
     * @param userLoginId
     * @return
     */
    public GroupListResponseDTO findUserGroupList(String userLoginId) {
        // 유저 그룹 아이디 정보 조회
        List<String> userGroupIds = userQueryService.getUserGroupIds(userLoginId);

        // 리트스 조회의 경우 약식 정보 응답
        List<GroupInfoResponseDTO> groupInfos = groupRepository.findAllById(userGroupIds)
                .stream().map(Group::parseResponseDTOSummary).toList();
        
        // 그룹 초대 목록 조회
        // mongoDB는 전달 한 id 순서대로 반환 정보의 정렬을 보장하지 않으므로 개별 조회해야 함.
        List<GroupInvitationQueueInfoResponseDTO> queues = groupInvitationQueueRepository.findAllByUserId(userLoginId)
                .stream().map(it -> {
                    Group group = groupRepository.findById(it.getGroupId()).orElseThrow(GroupNotFoundException::new);
                    
                    return new GroupInvitationQueueInfoResponseDTO(it.getId(), group.getId(), group.getName());

                }).toList();

        return new GroupListResponseDTO(groupInfos, queues);
    }

    /**
     * 그룹 유저 목록 조회
     * @param groupId
     * @return
     */
    public List<String> findGroupUserList(String groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        return group.getMemberIds();
    }

    public boolean isGroupOwner(String groupId, String userLoginId) {
        return groupRepository.existsByIdAndOwnerId(groupId, userLoginId);
    }
}
