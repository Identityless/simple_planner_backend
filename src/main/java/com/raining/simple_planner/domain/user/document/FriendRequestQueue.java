package com.raining.simple_planner.domain.user.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.raining.simple_planner.global.document.BaseDocument;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "friend_request_queue")
public class FriendRequestQueue extends BaseDocument{
    @Indexed(unique = false)
    private String pair1;   // 친구 추가 요청 보낸 사람
    @Indexed(unique = false)
    private String pair2;   // 친구 추가 요청 받은 사람
}
