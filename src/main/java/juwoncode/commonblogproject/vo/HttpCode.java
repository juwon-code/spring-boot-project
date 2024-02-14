package juwoncode.commonblogproject.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpCode {
    // 요청이 성공함
    public static final int HTTP_STATUS_OK = HttpStatus.OK.value();

    // 중복된 리소스가 존재함
    public static final int HTTP_STATUS_CONFLICT = HttpStatus.CONFLICT.value();

    // 서버 내부에 에러가 발생함
    public static final int HTTP_STATUS_INTERNAL_SEVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
}
