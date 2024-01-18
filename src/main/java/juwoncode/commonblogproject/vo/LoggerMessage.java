package juwoncode.commonblogproject.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoggerMessage {
    // 회원가입 서비스 로그
    public static final String REGISTER_SERVICE_SUCCESS_LOG = "회원 저장에 성공했습니다. 회원명: {}";
    public static final String REGISTER_SERVICE_FAILURE_LOG = "회원 저장에 실패했습니다. 사유: 중복된 회원. 회원명: {}";

    // 비밀번호 변경 서비스 로그
    public static final String CHANGE_PASSWORD_SERVICE_SUCCESS_LOG = "비밀번호 변경에 성공했습니다. 회원명: {}";
    public static final String CHANGE_PASSWORD_SERVICE_EMPTY_LOG = "비밀번호 변경에 실패했습니다. 사유: 존재하지 않는 회원. 회원명: {}";
    public static final String CHANGE_PASSWORD_SERVICE_WRONG_LOG = "비밀번호 변경에 실패했습니다. 사유: 잘못된 비밀번호. 회원명: {}";

    // 회원탈퇴 서비스 로그
    public static final String WITHDRAW_SERVICE_SUCCESS_LOG = "회원 삭제에 성공했습니다. 회원명: {}";
    public static final String WITHDRAW_SERVICE_EMPTY_LOG = "회원 삭제에 실패했습니다. 사유: 존재하지 않는 회원. 회원명: {}";
    public static final String WITHDRAW_SERVICE_WRONG_LOG = "회원 삭제에 실패했습니다. 사유: 잘못된 비밀번호. 회원명: {}";

    // 회원명 중복확인 서비스 로그
    public static final String CHECK_USERNAME_SERVICE_SUCCESS_LOG = "중복된 회원명이 존재합니다. 회원명: {}";
    public static final String CHECK_USERNAME_SERVICE_FAILURE_LOG = "중복된 회원명이 존재하지 않습니다. 회원명: {}";

    // 이메일 중복확인 서비스 로그
    public static final String CHECK_EMAIL_SERVICE_SUCCESS_LOG = "중복된 이메일이 존재합니다. 회원명: {}";
    public static final String CHECK_EMAIL_SERVICE_FAILURE_LOG = "중복된 이메일이 존재하지 않습니다. 회원명: {}";

    // 회원조회 로그
    public static final String FIND_MEMBER_WITH_USERNAME_SUCCESS_LOG = "회원 조회에 성공했습니다. 회원명: ";
    public static final String FIND_MEMBER_WITH_USERNAME_FAILURE_LOG = "회원 조회에 실패했습니다. 회원명: ";
    public static final String FIND_MEMBER_WITH_EMAIL_FAILURE_LOG = "회원 조회에 실패했습니다. 이메일: {}";
    
    // 메일 조회 로그
    public static final String FIND_EMAIL_WITH_CODE_AND_TYPE_SUCCESS_LOG = "메일 조회에 실패했습니다. 코드: {}, 타입: {}";
    
    // 인증 메일 만료 로그
    public static final String UPDATE_EMAIL_EXPIRED_SUCCESS_LOG = "인증 메일을 만료했습니다. 코드: {}, 타입: {}";
    public static final String UPDATE_EMAIL_EXPIRED_FAILURE_LOG = "이미 만료된 인증 메일입니다. 코드: {}, 타입: {}";
}
