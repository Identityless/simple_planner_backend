package com.raining.simple_planner.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegisterDTO {
    private String id;
    private String password;
    private String name;
    private String nickName;
    private String description;
}
