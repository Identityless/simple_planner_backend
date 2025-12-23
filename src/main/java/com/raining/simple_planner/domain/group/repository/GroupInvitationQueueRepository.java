package com.raining.simple_planner.domain.group.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raining.simple_planner.domain.group.document.GroupInvitationQueue;

@Repository
public interface GroupInvitationQueueRepository extends MongoRepository<GroupInvitationQueue, String>{
    List<GroupInvitationQueue> findAllByUserId(String userId);
}
