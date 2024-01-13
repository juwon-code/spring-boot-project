package juwoncode.commonblogproject.vo;

public final class ResponseMessage {
    // 회원명 중복 검사 메시지 :: 중복된 데이터가 존재함
    public static final String USERNAME_DUPLICATE_MESSAGE = "중복된 회원명입니다.";
    // 회원명 중복 검사 메시지 :: 중복된 데이터가 존재하지 않음
    public static final String USERNAME_NOT_DUPLICATE_MESSAGE = "사용가능한 회원명입니다.";
    // 이메일 중복 검사 메시지 :: 중복된 데이터가 존재함
    public static final String EMAIL_DUPLICATE_MESSAGE = "중복된 이메일입니다.";
    // 이메일 중복 검사 메시지 :: 중복된 데이터가 존재하지 않음
    public static final String EMAIL_NOT_DUPLICATE_MESSAGE = "사용가능한 이메일입니다.";
    // 회원가입 결과 메시지 :: 회원가입이 성공함
    public static final String REGISTER_SUCCESS_MESSAGE = "회원가입에 성공했습니다.";
    // 회원가입 결과 메시지 :: 회원가입이 실패함
    public static final String REGISTER_FAILURE_MESSAGE = "회원가입에 실패했습니다.";
    // 비밀번호 변경 결과 메시지 :: 비밀번호 변경이 성공함
    public static final String CHANGE_PASSWORD_SUCCESS_MESSAGE = "비밀번호 변경에 성공했습니다. 다시 로그인하세요.";
    // 비밀번호 변경 결과 메시지 :: 비밀번호 변경이 실패함
    public static final String CHANGE_PASSWORD_FAILURE_MESSAGE = "비밀번호 변경에 실패했습니다.";
}
