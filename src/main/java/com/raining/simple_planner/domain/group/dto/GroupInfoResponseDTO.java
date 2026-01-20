package com.raining.simple_planner.domain.group.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class GroupInfoResponseDTO {
    private String id;                              // 그룹 ID
    private String name;                            // 그룹 이름(GroovyRoom)
    private String ownerId;                         // 그룹 생성자(소유자) ID
    private String description;                     // 그룹 설명
    private List<String> planIds;                   // 그룹에 속한 플랜 ID 목록
    private List<String> memberIds;                 // 그룹 멤버 ID 목록
    private List<String> whiteBoardContentIds;      // 화이트보드 콘텐츠 ID 목록
}
