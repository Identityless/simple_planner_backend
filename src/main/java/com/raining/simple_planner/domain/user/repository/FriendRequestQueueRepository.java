package com.raining.simple_planner.domain.user.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raining.simple_planner.domain.user.document.FriendRequestQueue;

@Repository
public interface FriendRequestQueueRepository extends MongoRepository<FriendRequestQueue, Integer> {
    public boolean existsByPair1AndPair2(String pair1, String pair2);
    public FriendRequestQueue findByPair1(String pair1);
    public List<String> findAllPair2ByPair1(String pair1);
    public List<String> findAllPair1ByPair2(String pair2);
}
