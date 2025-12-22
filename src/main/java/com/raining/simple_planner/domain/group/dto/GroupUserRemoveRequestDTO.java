package com.raining.simple_planner.domain.group.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupUserRemoveRequestDTO {
    private String groupId;
    private List<String> removeUserIds;
}
