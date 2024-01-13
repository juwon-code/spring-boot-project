package juwoncode.commonblogproject.vo;

public final class LoggerMessage {
    // 회원가입 서비스 로그 :: 성공
    public static final String REGISTER_SERVICE_SUCCESS_LOG = "회원 저장에 성공했습니다. 회원명: {}";
    // 회원가입 서비스 로그 :: 실패
    public static final String REGISTER_SERVICE_FAILURE_LOG = "회원 저장에 실패했습니다. 회원명: {}";
    // 비밀번호 변경 서비스 로그 :: 성공
    public static final String CHANGE_PASSWORD_SERVICE_SUCCESS_LOG = "비밀번호 변경에 성공했습니다. 회원명: {}";
    // 비밀번호 변경 서비스 로그 :: 실패
    public static final String CHANGE_PASSWORD_SERVICE_FAILURE_LOG = "비밀번호 변경에 실패했습니다. 회원명: {}";
    // 회원탈퇴 서비스 로그 :: 성공
    public static final String WITHDRAW_SERVICE_SUCCESS_LOG = "회원 삭제에 성공했습니다. 회원명: {}";
    // 회원탈퇴 서비스 로그 :: 실패
    public static final String WITHDRAW_SERVICE_FAILURE_LOG = "회원 삭제에 실패했습니다. 회원명: {}";
    // 회원명 중복확인 서비스 로그 :: 중복
    public static final String CHECK_USERNAME_SERVICE_SUCCESS_LOG = "중복된 회원명이 존재합니다. 회원명: {}";
    // 회원명 중복확인 서비스 로그 :: 중복되지 않음
    public static final String CHECK_USERNAME_SERVICE_FAILURE_LOG = "중복된 회원명이 존재하지 않습니다. 회원명: {}";
    // 이메일 중복확인 서비스 로그 :: 중복
    public static final String CHECK_EMAIL_SERVICE_SUCCESS_LOG = "중복된 이메일이 존재합니다. 회원명: {}";
    // 이메일 중복확인 서비스 로그 :: 중복되지 않음
    public static final String CHECK_EMAIL_SERVICE_FAILURE_LOG = "중복된 이메일이 존재하지 않습니다. 회원명: {}";

    // 회원명으로 회원조회 로그 :: 성공
    public static final String FIND_MEMBER_WITH_USERNAME_SUCCESS_LOG = "회원 조회에 성공했습니다. 회원명: {}";
    // 회원명으로 회원조회 로그 :: 실패
    public static final String FIND_MEMBER_WITH_USERNAME_FAILURE_LOG = "회원 조회에 실패했습니다. 회원명: {}";
}
