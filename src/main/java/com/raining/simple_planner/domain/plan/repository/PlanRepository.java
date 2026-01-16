package com.raining.simple_planner.domain.plan.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raining.simple_planner.domain.plan.document.Plan;

@Repository
public interface PlanRepository extends MongoRepository<Plan, String> {
    
}
