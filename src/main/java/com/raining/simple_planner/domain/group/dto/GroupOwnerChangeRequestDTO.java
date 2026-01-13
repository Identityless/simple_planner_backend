package com.raining.simple_planner.domain.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupOwnerChangeRequestDTO {
    private String newGroupOwnerLoginId;
    private String groupId;
}
