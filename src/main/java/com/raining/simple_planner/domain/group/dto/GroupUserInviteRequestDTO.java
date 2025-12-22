package com.raining.simple_planner.domain.group.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupUserInviteRequestDTO {
    private String groupId;
    private List<String> inviteUserIds;
}
