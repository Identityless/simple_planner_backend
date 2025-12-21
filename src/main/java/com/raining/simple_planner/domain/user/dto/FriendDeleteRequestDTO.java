package com.raining.simple_planner.domain.user.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDeleteRequestDTO {
    private List<String> deleteIds;
}
