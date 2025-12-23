package com.raining.simple_planner.domain.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GroupInvitationQueueInfoResponseDTO {
    private String invitationId;        // 초대 큐 ID
    private String groupId;             // 그룹 ID
    private String groupName;           // 그루비룸
}
