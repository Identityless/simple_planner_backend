package com.raining.simple_planner.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserGroupUpdateDTO {
    private String userId;
    private String groupId;
}
