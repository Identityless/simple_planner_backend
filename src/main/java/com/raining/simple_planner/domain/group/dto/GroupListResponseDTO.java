package com.raining.simple_planner.domain.group.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupListResponseDTO {
    List<GroupInfoResponseDTO> groups;
    List<GroupInvitationQueueInfoResponseDTO> invitations;
}
