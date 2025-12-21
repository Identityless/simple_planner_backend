package com.raining.simple_planner.domain.user.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "friend_request_queue")
public class FriendRequestQueue {
    @Id @Indexed(unique = true)
    private Integer id;
    @Indexed(unique = false)
    private String pair1;
    @Indexed(unique = false)
    private String pair2;
}
