package com.raining.simple_planner.domain.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.raining.simple_planner.domain.user.document.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllById(List<String> ids);
    Optional<User> findByUserTag(String userTag);
    Boolean existsByUserTag(String userTag);
    Boolean existsByNickName(String nickName);
}
