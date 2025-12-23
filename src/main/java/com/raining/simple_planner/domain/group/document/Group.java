package com.raining.simple_planner.domain.group.document;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.raining.simple_planner.domain.group.dto.GroupInfoResponseDTO;
import com.raining.simple_planner.global.document.BaseDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "group")
public class Group extends BaseDocument{
    private String name;                            // 그룹 이름(GroovyRoom)
    private String ownerId;                         // 그룹 생성자(소유자) ID
    private String description;                     // 그룹 설명
    private List<Integer> planIds;                  // 그룹에 속한 플랜 ID 목록
    private List<String> memberIds;                 // 그룹 멤버 ID 목록
    private List<Integer> whiteBoardContentIds;     // 화이트보드 콘텐츠 ID 목록

    public GroupInfoResponseDTO parseResponseDTODetail() {
        return GroupInfoResponseDTO.builder()
                .id(this.getId())
                .name(name)
                .ownerId(ownerId)
                .description(description)
                .planIds(planIds)
                .memberIds(memberIds)
                .whiteBoardContentIds(whiteBoardContentIds)
                .build();
    }

    public GroupInfoResponseDTO parseResponseDTOSummary() {
        return GroupInfoResponseDTO.builder()
                .id(this.getId())
                .name(name)
                .build();
    }
}
