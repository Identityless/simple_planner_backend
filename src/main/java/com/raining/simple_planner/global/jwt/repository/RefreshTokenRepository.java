package com.raining.simple_planner.global.jwt.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raining.simple_planner.global.jwt.document.RefreshToken;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
}
