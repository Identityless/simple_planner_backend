package com.raining.simple_planner.global.result;



import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // Global
    TOKEN_REFRESH_SUCCESS("G001", "토큰 갱신 성공"),
    TOKEN_VALIDATE_SUCCESS("G002", "토큰 유효성 검사 성공"),

    // USER
    USER_REGISTRATION_SUCCESS("M001", "사용자 등록 성공"),
    USER_ACCOUNT_NOT_DUPLICATED("M002", "회원 아이디 중복되지않음"),
    USER_LOGIN_SUCCESS("M003", "회원 로그인 성공"),
    USER_LOGOUT_SUCCESS("M004", "회원 로그아웃 성공"),
    USER_UPDATE_SUCCESS("M005", "회원 프로필 업데이트 성공"),
    USER_FIND_SUCCESS("M006", "회원 조회 성공"),
    USER_LIST_REQUEST_SUCCESS("M007", "회원 리스트 조회 성공"),
    USER_DELETE_SUCCESS("M008", "회원 삭제 성공"),
    USER_PASSWORD_UPDATE_SUCCESS("M009", "회원 비밀번호 업데이트 성공"),
    USER_PROFILE_IMAGE_UPDATE_SUCCESS("M010", "회원 프로필 이미지 업데이트 성공"),
    USER_PASSWORD_CHANGE_SUCCESS("M011", "회원 비밀번호 변경 성공"),

    // FRIEND
    FRIEND_REQUEST_SUCCESS("F001", "친구 요청 성공"),
    FRIEND_REQUEST_ACCEPT_SUCCESS("F002", "친구 요청 수락 성공"),
    FRIEND_LIST_REQUEST_SUCCESS("F003", "친구 리스트 조회 성공"),
    FRIEND_DELETE_SUCCESS("F004", "친구 삭제 성공"),

    // GROUP
    GROUP_REGISTRATION_SUCCESS("P001", "그룹 등록 성공"),
    GROUP_UPDATE_SUCCESS("P002", "그룹 정보 수정 성공"),
    

    // Role
    // ROLE_REGISTRATION_SUCCESS("R001", "역할 등록 성공"),
    // ROLE_UPDATE_SUCCESS("R002", "역할 수정 성공"),
    // ROLE_DELETE_SUCCESS("R003", "역할 삭제 성공"),
    // ROLE_FIND_SUCCESS("R004", "역할 조회 성공"),
    // ROLE_LIST_REQUEST_SUCCESS("R005", "역할 리스트 조회 성공"),
    ;

    private final String code;
    private final String message;

}

