package juwoncode.commonblogproject.vo;

public final class ResponseMessage {
    // 회원명 중복 검사 응답 메시지
    public static final String USERNAME_DUPLICATE_MESSAGE = "중복된 회원명입니다.";
    public static final String USERNAME_NOT_DUPLICATE_MESSAGE = "사용가능한 회원명입니다.";

    // 이메일 중복 검사 응답 메시지
    public static final String EMAIL_DUPLICATE_MESSAGE = "중복된 이메일입니다.";
    public static final String EMAIL_NOT_DUPLICATE_MESSAGE = "사용가능한 이메일입니다.";

    // 회원가입 결과 응답 메시지
    public static final String REGISTER_SUCCESS_MESSAGE = "회원가입에 성공했습니다.";
    public static final String REGISTER_FAILURE_MESSAGE = "회원가입에 실패했습니다.";
    
    // 비밀번호 변경 결과 응답 메시지
    public static final String CHANGE_PASSWORD_SUCCESS_MESSAGE = "비밀번호 변경에 성공했습니다. 다시 로그인하세요.";
    public static final String CHANGE_PASSWORD_FAILURE_MESSAGE = "비밀번호 변경에 실패했습니다.";
}
