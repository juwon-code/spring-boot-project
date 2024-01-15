package juwoncode.commonblogproject.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static juwoncode.commonblogproject.vo.ExceptionMessage.*;
import static juwoncode.commonblogproject.vo.RegularExpression.*;

@Valid
public class MemberRequest {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginDto {
        @NotEmpty(message = USERNAME_EMPTY_EXCEPTION)
        private String username;

        @NotEmpty(message = PASSWORD_EMPTY_EXCEPTION)
        private String password;
    }

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

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ChangePasswordDto {
        @NotEmpty(message = USERNAME_EMPTY_EXCEPTION)
        @Pattern(regexp = USERNAME_PATTERN, message = USERNAME_WRONG_EXCEPTION)
        private String username;

        @NotEmpty(message = PASSWORD_EMPTY_EXCEPTION)
        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_WRONG_EXCEPTION)
        private String oldPassword;

        @NotEmpty(message = PASSWORD_EMPTY_EXCEPTION)
        @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_WRONG_EXCEPTION)
        private String newPassword;
    }

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
