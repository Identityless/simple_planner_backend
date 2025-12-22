package com.raining.simple_planner.domain.group.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GroupRegistrationRequestDTO {
    private String name;                            // 그룹 이름(GroovyRoom)
    private String description;                     // 그룹 설명
}
