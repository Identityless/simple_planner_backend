package com.raining.simple_planner.domain.user.service;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raining.simple_planner.domain.user.constant.Role;
import com.raining.simple_planner.domain.user.document.FriendRequestQueue;
import com.raining.simple_planner.domain.user.document.User;
import com.raining.simple_planner.domain.user.dto.ChangePasswordRequestDTO;
import com.raining.simple_planner.domain.user.dto.FriendAddRequestDTO;
import com.raining.simple_planner.domain.user.dto.FriendDeleteRequestDTO;
import com.raining.simple_planner.domain.user.dto.UserGroupUpdateDTO;
import com.raining.simple_planner.domain.user.dto.UserInfoUpdateRequestDTO;
import com.raining.simple_planner.domain.user.dto.UserRegisterDTO;
import com.raining.simple_planner.domain.user.exception.FriendRequestNotFoundException;
import com.raining.simple_planner.domain.user.exception.UserNotFoundException;
import com.raining.simple_planner.domain.user.repository.FriendRequestQueueRepository;
import com.raining.simple_planner.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UserRepository userRepository;
    private final FriendRequestQueueRepository friendRequestQueueRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param userRegisterDTO
     */
    @Transactional
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        // User 등록 로직 구현
        User user = User.builder()
                .id(userRegisterDTO.getId())
                .name(userRegisterDTO.getName())
                .nickName(userRegisterDTO.getNickName())
                .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .userTag(generateUniqueUserTag())
                .role(Role.ROLE_USER) // 가입 유저의 레벨은 기본적으로 일반사용자
                .build();
        
        log.info("회원가입 | ID : {}, NickName : {}, UserTag : {}", user.getId(), user.getNickName(), user.getUserTag());
        userRepository.save(user);
    }

    /**
     * 비밀번호 변경
     * @param userId
     * @param newPassword
     */
    @Transactional
    public void changePassword(String userId, ChangePasswordRequestDTO changePasswordRequestDTO) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 비밀번호 검사
        if (!passwordEncoder.matches(changePasswordRequestDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
        userRepository.save(user);
        log.info("비밀번호 변경 | ID : {}", userId);
    }

    /**
     * 유저 정보 수정
     * @param userId
     * @param userInfoUpdateRequestDTO
     */
    @Transactional
    public void updateUserInfo(String userId, UserInfoUpdateRequestDTO userInfoUpdateRequestDTO) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 닉네임 변경
        if (userInfoUpdateRequestDTO.getNickName() != null && !userInfoUpdateRequestDTO.getNickName().isEmpty()) {
            user.setNickName(userInfoUpdateRequestDTO.getNickName());
        }

        // 설명 변경
        if (userInfoUpdateRequestDTO.getDescription() != null) {
            user.setDescription(userInfoUpdateRequestDTO.getDescription());
        }

        userRepository.save(user);
        log.info("유저 정보 수정 | ID : {}", userId);
    }

    /**
     * 친구 추가 요청 저장
     * @param requestDTO
     */
    @Transactional
    public void saveFriendAddRequest(FriendAddRequestDTO requestDTO) {
        // 중복 요청 방지
        if (friendRequestQueueRepository.existsByPair1AndPair2(requestDTO.getOwnId(), requestDTO.getFriendId()) ||
                friendRequestQueueRepository.existsByPair1AndPair2(requestDTO.getFriendId(), requestDTO.getOwnId())) {
            log.info("이미 친구 요청이 존재합니다. | OwnId : {}, FriendId : {}", requestDTO.getOwnId(), requestDTO.getFriendId());
            return; // 이미 요청이 존재하면 저장하지 않음
        }

        // 이미 친구인 경우 요청 저장하지 않음
        User ownUser = userRepository.findById(requestDTO.getOwnId()).orElseThrow(UserNotFoundException::new);
        if (ownUser.getFriends().contains(requestDTO.getFriendId())) {
            log.info("이미 친구인 사용자입니다. | OwnId : {}, FriendId : {}", requestDTO.getOwnId(), requestDTO.getFriendId());
            return; // 이미 친구인 경우 요청 저장하지 않음
        }

        FriendRequestQueue friendRequest = new FriendRequestQueue();
        friendRequest.setPair1(requestDTO.getOwnId());
        friendRequest.setPair2(requestDTO.getFriendId());

        log.info("친구 요청 저장 | OwnId : {}, FriendId : {}", requestDTO.getOwnId(), requestDTO.getFriendId());

        friendRequestQueueRepository.save(friendRequest);
    }

    /**
     * 친구 요청 수락
     * @param requestId
     */
    @Transactional
    public void acceptFriendRequest(int requestId) {
        FriendRequestQueue request = friendRequestQueueRepository.findById(requestId)
                .orElseThrow(FriendRequestNotFoundException::new);

        User user1 = userRepository.findById(request.getPair1()).orElseThrow(UserNotFoundException::new);
        User user2 = userRepository.findById(request.getPair2()).orElseThrow(UserNotFoundException::new);

        // 친구 추가
        if (!user1.getFriends().contains(user2.getId())) {
            user1.getFriends().add(user2.getId());
        }
        if (!user2.getFriends().contains(user1.getId())) {
            user2.getFriends().add(user1.getId());
        }
        userRepository.save(user1);
        userRepository.save(user2);

        log.info("친구 요청 수락 | User1 ID : {}, User2 ID : {}", user1.getId(), user2.getId());
    }

    @Transactional
    public void deleteFriends(String userId, FriendDeleteRequestDTO friendDeleteRequestDTO) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        for (String deleteId : friendDeleteRequestDTO.getDeleteIds()) {
            User friend = userRepository.findById(deleteId).orElseThrow(UserNotFoundException::new);

            // 내 친구 목록에서 삭제
            user.getFriends().remove(deleteId);
            // 상대방 친구 목록에서 나 삭제
            friend.getFriends().remove(userId);

            userRepository.save(friend);
        }
        userRepository.save(user);

        log.info("친구 삭제 | User ID : {}, Deleted IDs : {}", userId, friendDeleteRequestDTO.getDeleteIds().toString());

    }

    @Transactional
    public void addUserGroup(UserGroupUpdateDTO userGroupUpdateDTO) {
        User user = userRepository.findById(userGroupUpdateDTO.getUserId()).orElseThrow(UserNotFoundException::new);

        user.getGroupKeys().add(userGroupUpdateDTO.getGroupId());

        log.info("유저 그룹 추가 | {}", userGroupUpdateDTO.toString());
    }

    @Transactional void deleteUserGroup(UserGroupUpdateDTO userGroupUpdateDTO) {
        User user = userRepository.findById(userGroupUpdateDTO.getUserId()).orElseThrow(UserNotFoundException::new);

        user.getGroupKeys().remove(userGroupUpdateDTO.getGroupId());

        log.info("유저 그룹 삭제 | {}", userGroupUpdateDTO.toString());
    }

    private String generateUniqueUserTag() {
        // 고유한 userTag 생성 로직 구현
        // 랜덤 숫자 생성 후 중복 체크
        String userTag;
        boolean exists = false;
        do {
        userTag = generateRandomUserTag();
        exists = userRepository.existsByUserTag(userTag);
        } while (exists);

        return userTag;
    }

    private String generateRandomUserTag() {
        // 랜덤한 userTag 생성 로직 구현
        // #000000 형식으로 랜덤 숫자 생성
        Random random = new Random();
        int number = random.nextInt(1000000);
        String userTag = String.format("#%06d", number);

        return userTag;
    }
}
