package com.raining.simple_planner.domain.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raining.simple_planner.domain.user.document.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByLoginId(List<String> LoginIds);
    Optional<User> findByUserTag(String userTag);
    Boolean existsByLoginId(String loginId);
    Boolean existsByUserTag(String userTag);
    Boolean existsByNickName(String nickName);
    Optional<User> findByLoginId(String loginId);
}
