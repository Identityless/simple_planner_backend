package com.raining.simple_planner.domain.group.document;

import org.springframework.data.mongodb.core.mapping.Document;

import com.raining.simple_planner.global.document.BaseDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Document(collation = "group_invitation_queue")
public class GroupInvitationQueue extends BaseDocument{
    private String groupId;
    private String userId;
}
