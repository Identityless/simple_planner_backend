package com.raining.simple_planner.global.jwt.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.raining.simple_planner.global.jwt.document.BlackList;

public interface BlackListRepository extends MongoRepository<BlackList, String> {
}
