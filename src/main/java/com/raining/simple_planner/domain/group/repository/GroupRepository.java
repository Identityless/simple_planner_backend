package com.raining.simple_planner.domain.group.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raining.simple_planner.domain.group.document.Group;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    
}
