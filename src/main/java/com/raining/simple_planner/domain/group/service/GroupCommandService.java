package com.raining.simple_planner.domain.group.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raining.simple_planner.domain.group.document.Group;
import com.raining.simple_planner.domain.group.dto.GroupRegistrationRequestDTO;
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
}
