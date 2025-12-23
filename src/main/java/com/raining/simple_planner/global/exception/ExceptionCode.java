
package com.raining.simple_planner.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    // Global
    INVALID_INPUT_VALUE(400, "G001", "유효하지 않은 입력값입니다."),
    INTERNAL_SERVER_ERROR(500, "G002", "서버 내부 오류입니다."),
    FILE_CONVERT_ERROR(500, "G003", "파일 변환 오류입니다."),
    UN_AUTHORIZED_ACCESS(401, "G004", "권한이 없습니다."),
    INVALID_TOKEN(401, "G005", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(4001, "G006", "만료된 토큰입니다."),
    TOKEN_REFRESH_FAIL(4002, "G007", "토큰 갱신에 실패했습니다."),

    // USER
    USER_NOT_FOUND(400, "M001", "사용자를 찾을 수 없습니다."),
    USER_ACCOUNT_DUPLICATED(400, "M002", "회원 아이디 중복"),
    USER_UPDATE_FAIL(400, "M003", "회원 프로필 업데이트 실패"),
    USER_LIST_REQUEST_FAIL(500, "M004", "회원 리스트 요청 실패"),
    USER_PASSWORD_WRONG(401, "M005", "비밀번호가 틀렸습니다."),
    USER_AUTH_FAIL(401, "M006", "회원 인증 실패"),
    USER_NICKNAME_DUPLICATED(400, "M007", "회원 닉네임 중복"),
    USER_LOGOUT_FAIL(499, "M008", "회원 로그아웃 실패"),
    USER_NO_PERMISSION(403, "M009", "권한이 없습니다"),

    // FRIEND
    FRIEND_REQUEST_ALREADY_EXISTS(400, "F001", "이미 존재하는 친구 요청입니다."),
    FRIEND_REQUEST_NOT_FOUND(400, "F002", "친구 요청을 찾을 수 없습니다."),
    FRIEND_ALREADY_ADDED(400, "F003", "이미 추가된 친구입니다."),

    // GROUP
    GROUP_NOT_FOUND(400, "P001", "그룹을 찾을 수 없습니다."),
    GROUP_LIST_REQUEST_FAIL(500, "P002", "그룹 리스트 요청 실패"),
    GROUP_REGISTRATION_FAIL(500, "P003", "그룹 등록 실패"),
    GROUP_UPDATE_FAIL(500, "P004", "그룹 수정 실패"),
    GROUP_DELETE_FAIL(500, "P005", "그룹 삭제 실패"),
    GROUP_DUPLICATED(400, "P006", "이미 등록되어있는 그룹입니다"),
    GROUP_NO_PERMISSION(403, "P007", "그룹에 대한 권한이 없습니다"),
    GROUP_INVITATION_QUEUE_NOT_FOUND(500, "P008", "그룹 초대 내역을 찾을 수 없습니다"),
    GROUP_ALREADY_INVITED(400, "P009", "이미 그룹에 초대된 사용자입니다"),

    // Role
    // ROLE_NOT_FOUND(400, "R001", "역할을 찾을 수 없습니다."),
    // ROLE_LIST_REQUEST_FAIL(500, "R006", "역할 리스트 요청 실패"),
    // ROLE_REGISTRATION_FAIL(500, "R002", "역할 등록 실패"),
    // ROLE_UPDATE_FAIL(500, "R003", "역할 수정 실패"),
    // ROLE_DELETE_FAIL(500, "R004", "역할 삭제 실패"),
    // ROLE_DUPLICATED(400, "R005", "이미 등록되어있는 역할입니다."),
    ;

    private final int status;
    private final String code;
    private final String message;

}
