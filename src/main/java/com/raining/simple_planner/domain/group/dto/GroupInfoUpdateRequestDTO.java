package com.raining.simple_planner.domain.group.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupInfoUpdateRequestDTO {
    private String groupId;
    private String name;
    private String description;
    private String ownerId;
}
