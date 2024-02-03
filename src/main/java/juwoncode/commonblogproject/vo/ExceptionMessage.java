package juwoncode.commonblogproject.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    // 회원명 예외 메시지
    public static final String USERNAME_WRONG_EXCEPTION = "회원명 파라미터 형식이 올바르지 않습니다.";
    public static final String USERNAME_EMPTY_EXCEPTION = "회원명 파라미터가 비어있습니다.";

    // 비밀번호 예외 메시지
    public static final String PASSWORD_WRONG_EXCEPTION = "비밀번호 파라미터 형식이 올바르지 않습니다.";
    public static final String PASSWORD_EMPTY_EXCEPTION = "비밀번호 파라미터가 비어있습니다.";

    // 이메일 예외 메시지
    public static final String EMAIL_WRONG_EXCEPTION = "이메일 파라미터 형식이 올바르지 않습니다.";
    public static final String EMAIL_EMPTY_EXCEPTION = "이메일 파라미터가 비어있습니다.";

    // 메일전송 예외 메시지
    public static final String EMAIL_CODE_EMPTY_EXCEPTION = "메일 코드 파라미터가 비어있습니다.";
    public static final String EMAIL_TYPE_EMPTY_EXCEPTION = "메일 타입 파라미터가 비어있습니다.";
    public static final String MEMBER_NULL_EXCEPTION = "회원 파라미터가 비어있습니다.";

    // API 응답 예외 메시지
    public static final String HTTP_CODE_EMPTY_EXCEPTION = "응답 HTTP 코드가 누락되었습니다.";
    public static final String API_MESSAGE_EMPTY_EXCEPTION = "응답 메시지가 누락되었습니다.";
    public static final String API_BODY_EMPTY_EXCEPTION = "응답 바디가 누락되었습니다.";
}
