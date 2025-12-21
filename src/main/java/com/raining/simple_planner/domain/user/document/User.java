package com.raining.simple_planner.domain.user.document;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.raining.simple_planner.domain.user.constant.Role;
import com.raining.simple_planner.domain.user.dto.UserInfoResponseDTO;
import com.raining.simple_planner.global.document.BaseDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User extends BaseDocument {
    @Id @Indexed(unique = true)
    private String id;
    private String password;
    private Role role;
    private String name;
    private String nickName;
    private String description;
    @Indexed(unique = true)
    private String userTag; // #000000 형식의 고유 태그
    private List<String> groupKeys;
    private List<String> friends; // 친구 목록을 userTag의 리스트로 저장

    public UserInfoResponseDTO toResponseDTO() {
        return UserInfoResponseDTO.builder()
            .name(this.name)
            .nickName(this.nickName)
            .userTag(this.userTag)
            .build();
    }

    public UserInfoResponseDTO toDetailResponseDTO() {
        return UserInfoResponseDTO.builder()
            .id(this.id)
            .name(this.name)
            .nickName(this.nickName)
            .userTag(this.userTag)
            .role(this.role)
            .groupKeys(this.groupKeys)
            .friends(this.friends)
            .build();
    }
}
