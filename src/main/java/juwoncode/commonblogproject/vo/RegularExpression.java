package juwoncode.commonblogproject.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegularExpression {
    // 회원명 정규식 :: 6 ~ 20자 이내의 영문자 + 숫자만을 허용
    public static final String USERNAME_PATTERN = "^[a-z]+[a-z0-9]{5,19}$";

    // 비밀번호 정규식 :: 8 ~ 16자 이내의 대소문자 + 특수문자 + 숫자를 하나씩 포함
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$";
}
