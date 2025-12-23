package com.raining.simple_planner.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raining.simple_planner.domain.user.document.User;
import com.raining.simple_planner.domain.user.dto.FriendListResponseDTO;
import com.raining.simple_planner.domain.user.dto.UserInfoResponseDTO;
import com.raining.simple_planner.domain.user.exception.UserNotFoundException;
import com.raining.simple_planner.domain.user.repository.FriendRequestQueueRepository;
import com.raining.simple_planner.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;
    private final FriendRequestQueueRepository friendRequestQueueRepository;

    public boolean isNickNameExists(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    public boolean isLoginIdExists(String LoginId) {
        return userRepository.existsByLoginId(LoginId);
    }

    /**
     * 아이디로 유저 조회 (내부 조회용)
     * @param id
     * @return
     */
    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 유저 정보 조회 (api호출 응답용)
     * @param type
     * @param value
     * @param isOwn
     * @return
     */
    public UserInfoResponseDTO findUser(String type, String value, boolean isOwn) {
        User user = null;
        switch (type) {
            case "id":
                user = userRepository.findByLoginId(value).orElseThrow(UserNotFoundException::new);
                break;
            case "tag":
                user = userRepository.findByUserTag(value).orElseThrow(UserNotFoundException::new);
                break;
        }

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (isOwn) {
            return user.toDetailResponseDTO();
        } else {
            return user.toResponseDTO();
        }

    }

    /**
     * 친구 목록 조회
     * @param userLoginId
     * @return
     */
    public FriendListResponseDTO getFriendList(String userLoginId) {
        // 유저 정보 조회
        User user = userRepository.findByLoginId(userLoginId).orElseThrow(UserNotFoundException::new);

        List<UserInfoResponseDTO> friends = userRepository.findAllByLoginId(user.getFriends()).stream()
                .map(User::toResponseDTO)
                .toList();
        List<UserInfoResponseDTO> friendRequestUsers = findFriendRequestList(userLoginId);

        FriendListResponseDTO responseDTO = new FriendListResponseDTO();
        responseDTO.setFriends(friends);
        responseDTO.setFriendRequestUsers(friendRequestUsers);

        return responseDTO;
    }

    private List<UserInfoResponseDTO> findFriendRequestList(String userLoginId) {
        List<String> friendRequestIds = friendRequestQueueRepository.findAllPair1ByPair2(userLoginId);
        return userRepository.findAllByLoginId(friendRequestIds).stream()
                .map(User::toResponseDTO)
                .toList();
    }

    /**
     * 유저 그룹 아이디 목록 조회
     * @param userLoginId
     * @return
     */
    public List<String> getUserGroupIds(String userLoginId) {
        User user = userRepository.findByLoginId(userLoginId).orElseThrow(UserNotFoundException::new);

        return user.getGroupKeys();
    }

}