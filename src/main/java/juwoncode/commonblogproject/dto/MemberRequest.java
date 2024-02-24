package juwoncode.commonblogproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import static juwoncode.commonblogproject.vo.ExceptionMessage.*;
import static juwoncode.commonblogproject.vo.RegularExpression.*;

@Valid
public class MemberRequest {
    /**
     * 로그인 요청 파라미터를 담고있는 DTO 클래스.<br>
     * <pre>
     * - username: 회원명, 비워둘 수 없음.
     * - password: 비밀번호, 비워둘 수 없음.
     * </pre>
     * @see
     *      NotEmpty
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDto {
        @NotEmpty(message = USERNAME_EMPTY_EXCEPTION)
        private String username;

        @NotEmpty(message = PASSWORD_EMPTY_EXCEPTION)
        private String password;
    }

    /**
     * 회원가입 요청 파라미터를 담고있는 DTO 클래스.<br>
     * <pre>
     * - username: 회원명, 비워둘 수 없음, 회원명 패턴 적용.
     * - password: 비밀번호, 비워둘 수 없음. 비밀번호 패턴 적용.
     * - email: 이메일, 비워둘 수 없음. 이메일 패턴 적용.
     * </pre>
     * @see
     *      juwoncode.commonblogproject.vo.RegularExpression#USERNAME_PATTERN
     * @see
     *      juwoncode.commonblogproject.vo.RegularExpression#PASSWORD_PATTERN
     * @see
     *      NotEmpty
     * @see
     *      Email
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class RegisterDto {
        @NotEmpty(message = USERNAME_EMPTY_EXCEPTION)
        @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_WRONG_EXCEPTION)
        private String username;

        @NotEmpty(message = PASSWORD_EMPTY_EXCEPTION)
        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_WRONG_EXCEPTION)
        private String password;

        @NotEmpty(message = EMAIL_EMPTY_EXCEPTION)
        @Email(message = EMAIL_WRONG_EXCEPTION)
        private String email;
    }

    /**
     * 비밀번호 변경 요청 파라미터를 담고있는 DTO 클래스.<br>
     * <pre>
     * - username: 회원명, 비워둘 수 없음.
     * - oldPassword: 기존 비밀번호, 비워둘 수 없음.
     * - newPassword: 새 비밀번호, 비워둘 수 없음, 비밀번호 패턴 적용.
     * </pre>
     * @see
     *      NotEmpty
     * @see
     *      juwoncode.commonblogproject.vo.RegularExpression#USERNAME_PATTERN
     * @see
     *      juwoncode.commonblogproject.vo.RegularExpression#PASSWORD_PATTERN
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChangePasswordDto {
        @NotEmpty(message = USERNAME_EMPTY_EXCEPTION)
        private String username;

        @NotEmpty(message = PASSWORD_EMPTY_EXCEPTION)
        private String oldPassword;

        @NotEmpty(message = PASSWORD_EMPTY_EXCEPTION)
        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_WRONG_EXCEPTION)
        private String newPassword;
    }

    /**
     * 회원탈퇴 요청 파라미터를 담고있는 DTO 클래스.<br>
     * <pre>
     * - username: 회원명, 비워둘 수 없음.
     * - password: 비밀번호, 비워둘 수 없음.
     * </pre>
     * @see
     *      NotEmpty
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class WithdrawDto {
        @NotEmpty(message = USERNAME_EMPTY_EXCEPTION)
        private String username;

        @NotEmpty(message = PASSWORD_EMPTY_EXCEPTION)
        private String password;
    }
}
