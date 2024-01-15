package juwoncode.commonblogproject.vo;

import org.springframework.http.HttpStatus;

public final class HttpCode {
    // 요청이 성공함
    public static final int HTTP_STATUS_OK = HttpStatus.OK.value();

    // 중복된 리소스가 존재함
    public static final int HTTP_STATUS_CONFLICT = HttpStatus.CONFLICT.value();
}
