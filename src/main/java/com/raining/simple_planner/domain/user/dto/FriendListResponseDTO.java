package com.raining.simple_planner.domain.user.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendListResponseDTO {
    private List<UserInfoResponseDTO> friends;
    private List<UserInfoResponseDTO> friendRequestUsers;
}
