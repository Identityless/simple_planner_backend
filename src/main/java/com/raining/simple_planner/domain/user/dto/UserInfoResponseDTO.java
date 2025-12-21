package com.raining.simple_planner.domain.user.dto;

import java.util.List;

import com.raining.simple_planner.domain.user.constant.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponseDTO {
    private String id;
    private String name;
    private String nickName;
    private String userTag;
    private Role role;
    private List<String> groupKeys;
    private List<String> friends;
}
